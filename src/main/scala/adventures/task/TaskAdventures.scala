package adventures.task

import monix.eval.Task

import scala.concurrent.duration.FiniteDuration

/**
  * If a result 'A' is available synchronously, then that same result asynchronously
  * could be represented as a 'Task[A]'
  */
object TaskAdventures {

  /*
    Task is a data type to model and control effectful computations in a purely functional way.
    It is a powerful abstraction to run tasks in a non-blocking/async way.
    See task documentation for the 3.x series here https://monix.io/docs/3x/eval/task.html
    And the current https://monix.io/docs/current/eval/task.html
   */

  /**
    * 1.  Create a task which returns 43
    *
    * See https://monix.io/docs/3x/eval/task.html#simple-builders for ways to construct Tasks
    */
  def immediatelyExecutingTask(): Task[Int] = {
    ???
  }

  /**
    * 2.	Create a Task which when executed logs “hello world” (using `logger`)
    */
  def helloWorld(logger: String => Unit): Task[Unit] = {
    ???
  }

  /**
    * 3.	Create a Task which always fails.
    *
    * See https://monix.io/docs/3x/eval/task.html#taskraiseerror
    */
  def alwaysFailingTask(): Task[Unit] = {
    ???
  }

  /**
    * 4.	There is 1 remote service which will return you a task that provides the current temperature in celsius.
    *
    */
  def getCurrentTempInF(currentTemp: () => Task[Int]): Task[Int] = {
//    def cToF(c: Int) = c * 9 / 5 + 32

    ???
  }

  /**
    * 5.	There is 1 remote service which will return you a task that provides the current temperature in celsius.
    * The conversion is complex so we have decided to refactor it out to another remote microservice.
    * Make use of both of these services to return the current temperature in fahrenheit.
    */
  def getCurrentTempInFAgain(currentTemp: () => Task[Int], converter: Int => Task[Int]): Task[Int] = {
    ???
  }

  /**
    * 6. Computing the complexity of a string is a very expensive op.  Implement this function so that complexity
    * of the calculation will be done in parallel.  Sum the returned results to
    * provide the overall complexity for all Strings.  (try using operations from monix)
    *
    * Also, check out the spec for this to see how the Monix TestScheduler can be used to simulate the passage of time
    * in tests.
    */
  def calculateStringComplexityInParallel(strings: List[String], complexity: String => Task[Int]): Task[Int] = {
    ???
  }

  /**
    * 6.b. As above, but try to implement the parallel processing using the monix Applicative instance for Task
    * and the cats `sequence` function. (if you haven't heard of cats / sequence skip this - even if you have consider
    * this as optional).
    *
    * The following imports will help.
    * import cats.implicits._
    * implicit def parTaskApplicative: Applicative[eval.Task.Par] = Task.catsParallel.applicative
    *
    * Note that you will also need to convert from Task to Task.Par for the cats sequence operator to execute the tasks
    * in parallel. Look at the functions in eval.Task.Par
    */
  def calculateStringComplexityInParallelAgain(strings: List[String], complexity: String => Task[Int]): Task[Int] = {
    ???
  }

  /**
    * 7.	Write a function which given a Task, will reattempt that task after a specified delay for a maximum number of
    * attempts if the supplied Task fails.
    */
  def retryOnFailure[T](t: Task[T], maxRetries: Int, delay: FiniteDuration): Task[T] = {
    ???
  }

}
