package service.grpc

import kotlinx.coroutines.*
import mu.KotlinLogging
import java.util.concurrent.Executors.newFixedThreadPool

private val logger = KotlinLogging.logger {}
class SimpleHandManagementServiceImpl: SimpleHandManagementServiceImplBase(
        coroutineContext = newFixedThreadPool(1).asCoroutineDispatcher()
) {

    override suspend fun hello(request: SimpleMessage): SimpleMessage {
        logger.debug { "incoming hello request from ${request.contents}" }
        return SimpleMessage.newBuilder()
                .setContents("Hello " + request.contents)
                .build()
    }
}
