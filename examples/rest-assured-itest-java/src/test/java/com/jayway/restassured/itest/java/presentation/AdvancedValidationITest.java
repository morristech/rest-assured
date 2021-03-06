/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jayway.restassured.itest.java.presentation;

import com.jayway.restassured.itest.java.support.WithJetty;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.*;

public class AdvancedValidationITest extends WithJetty {

    @Test
    public void groceriesContainsChocolateAndCoffee() throws Exception {
        expect().
                body("shopping.category.find { it.@type == 'groceries' }", hasItems("Chocolate", "Coffee")).
        when().
                get("/shopping");
    }

    @Test
    public void groceriesContainsChocolateAndCoffeeUsingDoubleStarNotation() throws Exception {
        expect().
                body("**.find { it.@type == 'groceries' }", hasItems("Chocolate", "Coffee")).
        when().
                get("/shopping");
    }

    @Test
    public void advancedJsonValidation() throws Exception {
        expect().
                statusCode(allOf(greaterThanOrEqualTo(200), lessThanOrEqualTo(300))).
                root("store.book").
                body("findAll { book -> book.price < 10 }.title", hasItems("Sayings of the Century", "Moby Dick")).
                body("author.collect { it.length() }.sum()", equalTo(53)).
        when().
                get("/jsonStore");
    }

    @Test
    public void advancedJsonValidation2() throws Exception {
        expect().
                statusCode(allOf(greaterThanOrEqualTo(200), lessThanOrEqualTo(300))).
                root("store.book").
                body("findAll { book -> book.price < 10 }.title", hasItems("Sayings of the Century", "Moby Dick")).
                body("price.min()", equalTo(8.95f)).
                body("price.max()", equalTo(22.99f)).
                body("min { it.price }.title", equalTo("Sayings of the Century")).
                body("author*.length().sum()", equalTo(53)).
                body("author*.length().sum(2, { it * 2 })", is(108)).
        when().
                get("/jsonStore");
    }
}
