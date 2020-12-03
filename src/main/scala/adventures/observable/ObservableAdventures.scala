package adventures.observable

import adventures.observable.model.{PageId, PaginatedResult, SourceRecord, TargetRecord}
import monix.eval.Task
import monix.reactive.Observable

import scala.concurrent.Future

/**
  * If elements from a list can be operated on synchronously as a List[A], then the equivalent data structure where
  * those elements can be operated asynchronously could be represented as a Observable[A].
  *
  * The docs for Observable are online https://monix.io/docs/3x/reactive/observable.html.  Note they are very thin and
  * don't cover the majority of functions on Observable.  For those you are best exploring Observable in your IDE or
  * browsing https://monix.io/api/3.0/monix/reactive/Observable.html.
  *
  * Latest documentation can be found in https://monix.io/docs/current/
  *
  * These exercises will introduce you to a common problem ETL pipeline.  The steps to complete this are.
  * 1. Read from a external paginated datasource (no need to worry about duplicate data, assume data will only come through
  * once).
  * 2. Transform that data (filtering out any invalid data)
  * 3. Insert that data into elasticsearch (which has an efficient API to insert in batches of 5 - in reality its much more
  * but it keeps the test output small).
  */
object ObservableAdventures {

  /**
    * For this exercise, think (NO NEED TO IMPLEMENT) about how you would implement the following without Monix.
    *
    * Given an Iterable or record ids, how would you go about loading each of those records asynchronously?
    *
    * This exercise doesn't have to be implemented, but just think about what this would look like.  Would you model the
    * return type as:
    * - Iterable[TargetRecord] (what does this imply must happen?)
    * - Iterable[Future[TargetRecord]] (what is then responsible for handling the asynchronous nature?)
    *
    * What would you do if you needed back pressure (i.e. if something else consuming from the iterable slowed down, how
    * would this propagate?)
    */
  def iterablesAndConcurrency(records: Iterable[Int], load: Int => Future[SourceRecord]): Iterable[TargetRecord] = ???

  /**
    * Create an Observable which emits each element of the source list.
    *
    * Look at the helper functions in Observable: fromXYZ.
    */
  def listToObservable(records: List[SourceRecord]): Observable[SourceRecord] = ???

  /**
    * Transform all of the SourceRecords to TargetRecords.  If the price cannot be converted to a double,
    * then drop the Source element.
    *
    * Remember, parsing/transforming a String to a double is an unsafe operation that needs to be dealt with.
    *
    * @param sourceRecords
    * @return
    */
  def transform(sourceRecords: Observable[SourceRecord]): Observable[TargetRecord] = ???

  /**
    * Your elasticsearch supports saving batches of 5 records. This is a remote async call so the result is represented
    * by `Task`.
    *
    * Implement the following method so it calls elasticSearchLoad with batches of 5 records and returns the number
    * of loaded items, i.e. size of each batch loaded.
    *
    * Have a look at what Observable provides out of the box for this. Hint: buffering and evaluating tasks are the
    * concepts here.
    */
  def load(targetRecords: Observable[TargetRecord], elasticSearchLoad: Seq[TargetRecord] => Task[Unit]): Observable[Int] = ???

  /**
    * Elasticsearch supports saving batches of 5 records.  This is a remote async call so the result is represented
    * by `Task`.  Note that the elasticSearchLoad may fail (in practice this is pretty rare).  Rather than the Observable terminating with an error,
    * try using the Task retry logic you created earlier in TaskAdventures.
    *
    * Returns the number of records which were saved to elastic search.
    */
  def loadWithRetry(targetRecords: Observable[TargetRecord], elasticSearchLoad: Seq[TargetRecord] => Task[Unit]): Observable[Int] = {
    load(targetRecords, elasticSearchLoad)
  }

  /**
    * Consume the Observable.
    *
    * Monix
    * > models producer-consumer relationships, where you can have a single producer pushing data into one or multiple consumers
    *
    * We need a consumer to materialize and exhaust the stream.
    *
    * The final result should be the number of records which were saved to ElasticSearch.
    *
    * Hint: have a look at what's available in the monix.reactive.Consumer object.
    */
  def execute(loadedObservable: Observable[Int]): Task[Int] = ???

  /**
    * Create an Observable from which all records can be read.
    *
    * Earlier we created "listToObservable", but what if the source data comes from a paginated datasource.
    * In the following method `readPage` returns a PaginatedResult object that contains list of record and a
    * reference to the next page. You are required the read the records from ALL the pages and return them in a
    * single Observable.
    *
    * The first page of data can be obtained using `PageId.FirstPage`, after which you should follow the nextPage
    * references in the PaginatedResult.
    *
    * Look at
    * Observable.++ AND
    * Observable.tailRecM OR Observable.flatMap
    */
  def readFromPaginatedDatasource(readPage: PageId => Task[PaginatedResult]): Observable[SourceRecord] = ???

  /**
    * Lets say reading a page takes 1 second and loading a batch of records takes 1 second.  If there are 20 pages (each
    * of one load batch in size), how long will it take to execute?  Look for "Processing took XXms" in the logs.  Try
    * to reduce the overall time by doing the reads and writes in parallel.  Below is provided a sequential implementation
    * (assuming you have implemented the methods above).
    *
    * Hint: Read up on asynchronous boundaries in https://monix.io/api/3.0/monix/reactive/Observable.html
    */
  def readTransformAndLoadAndExecute(readPage: PageId => Task[PaginatedResult],
                                     elasticSearchLoad: Seq[TargetRecord] => Task[Unit]): Task[Int] = {
    // Note it wouldn't look like this in the prod code, but more a factor of combining our building blocks above.
    val readObservable = readFromPaginatedDatasource(readPage)
    val transformedObservable = transform(readObservable)
    execute(load(transformedObservable, elasticSearchLoad))
  }
}
