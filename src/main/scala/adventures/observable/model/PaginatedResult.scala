package adventures.observable.model

// Next page will be None when all changes have been read
final case class PaginatedResult(results: List[SourceRecord], nextPage: Option[PageId])
