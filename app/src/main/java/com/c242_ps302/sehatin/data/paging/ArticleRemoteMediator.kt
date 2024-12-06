package com.c242_ps302.sehatin.data.paging

//@OptIn(ExperimentalPagingApi::class)
//class ArticleRemoteMediator(
//    private val apiService: NewsApiService,
//    private val database: SehatinDatabase
//) : RemoteMediator<Int, ArticleEntity>() {
//
//    companion object {
//        private const val STARTING_PAGE_INDEX = 1
//    }
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, ArticleEntity>,
//    ): MediatorResult {
//        try {
//            val currentPage = when (loadType) {
//                LoadType.REFRESH -> {
//                    STARTING_PAGE_INDEX
//                }
//                LoadType.PREPEND -> TODO()
//                LoadType.APPEND -> TODO()
//            }
//
//            val response = apiService.getHeadlineNews(page = currentPage, pageSize = ITEMS_PER_PAGE)
//        } catch (e: Exception) {
//            return MediatorResult.Error(e)
//        }
//    }
//}