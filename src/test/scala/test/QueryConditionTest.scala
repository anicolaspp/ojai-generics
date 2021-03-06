package com.github.anicolaspp.ojai
package test

import org.ojai.store._
import org.scalatest.{FlatSpec, Matchers}

class QueryConditionTest extends FlatSpec with com.github.anicolaspp.ojai.OjaiTesting with Matchers {

  import QueryConditionExtensions._

//  DriverManager.registerDriver(com.mapr.ojai.store.impl.InMemoryDriver)

//  val connection = DriverManager.getConnection("ojai:anicolaspp:mem")

  "A query" should "is" in {
    val condition = connection
      .newCondition()
      .field("name") === "nico"

    val expected = connection
      .newCondition()
      .is("name", QueryCondition.Op.EQUAL, "nico")
      .build()
      .asJsonString()

    condition.build().asJsonString() should be(expected)
  }

  it should "compare" in {
    val condition = connection
      .newCondition()
      .greaterThan("age", 5)
      .build()
      .asJsonString()

    val expected = connection
      .newCondition()
      .is("age", QueryCondition.Op.GREATER, 5)
      .build()
      .asJsonString()

    condition should be(expected)
  }
}
