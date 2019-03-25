# ojai-generics

Thin, generic, scala idiomatic layer on top of MapR OJAI

`ojai-generics` presents a very thin layer on top of OJAI that easies working with OJAI from Scala by adding idiomatic 
Scala constructs. 

## QueryConditions Add-Ons

The OJAI API is written in Java and functions around `QueryCondition` use method overriding for different data types. In other words, 
in order to build a query, we need to know the exact type we are using at compile time, and while this seems like a good idea,
it is far from convient in most occations.

Let's look at some examples to show our case.

## Using `ojai-generics`

We can use `ojai-generics` to reduce the boilerplate code we are forced to write when using OJAI Java like API by using 
generics and idiomatic Scala.

Let's build an example from scratch so show in more details the advantages of using `ojai-generics`.

Suppose we want to create a `QueryCondition` for a value coming from a Spark DataFrame, normally coming as `Any`.

```scala

def buildEqualToCondition(field: String, value: Any)(implicit connection: Connection): QueryCondition = any match {
  case _: Int    => connection.newCondition().is(field, Op.EQUAL, value.asInstanceOf[Int]).build()
  case _: Double => connection.newCondition().is(field, Op.EQUAL, value.asInstanceOf[Double]).build()
  ....
}

def buildLessThanCondition(field: String, value: Any)(implicit connection: Connection): QueryCondition = any match {
  case _: Int    => connection.newCondition().is(field, Op.LESS_THAN, value.asInstanceOf[Int]).build()
  case _: Double => connection.newCondition().is(field, Op.LESS_THAN, value.asInstanceOf[Double]).build()
  ....
}
```

That really is a problem if we have many types and many operations, since the same we are doing for all types have to be done 
for all operations we have. 

Using `ojai-generics` we can do this as follow.

```scala

def buildEqualToCondition(field: String, value: Any)(implicit connection: Connection): QueryCondition = 
  connection.newCondition().field(field) === value
  
def buildLessThanCondition(field: String, value: Any)(implicit connection: Connection): QueryCondition =
  connection.newCondition().field(field) < value

```

Notice that we have reduced the priously shown code by removing the pattern matching on the type. That means, we are using a
generic API that is able to accept all possible types. Also, `ojai-genercis` takes care of the castings and convertions for us. In addition, it adds operators so we can think about these operations in a very natural way. 

If we prefer a more Java like API, we can still do the following without loosing type safty and generics. 

```scala
def buildEqualToCondition(field: String, value: Any)(implicit connection: Connection): QueryCondition = 
  connection.newCondition().equalTo(field, value)
  
def buildLessThanCondition(field: String, value: Any)(implicit connection: Connection): QueryCondition =
  connection.newCondition().lessThan(field, value)
```

These options are aliases that produce the same results as before just using a more verbose API. 

## Important Notice

It is very important to notice, that we are only adding a thin layer on top of the existing OJAI API. Everything that works there will work while using our library. We only add extended functionality. We don't modify existing functionality in any way.

