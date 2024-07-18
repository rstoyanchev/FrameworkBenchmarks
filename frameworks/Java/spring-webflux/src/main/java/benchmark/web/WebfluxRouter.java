package benchmark.web;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

@Configuration
public class WebfluxRouter {

	@Bean
	public HttpHandler httpHandler(RouterFunction<ServerResponse> route, ServerFilter serverFilter) {
		return WebHttpHandlerBuilder.webHandler(RouterFunctions.toWebHandler(route))
				.filter(serverFilter)
				.build();
	}

	@Bean
	public RouterFunction<ServerResponse> route(WebfluxHandler handler) {
		return request -> {
			HandlerFunction<ServerResponse> fn = switch (request.uri().getRawPath()) {
				case "/plaintext" -> handler::plaintext;
				case "/json" -> handler::json;
				case "/db" -> handler::db;
				case "/queries" -> handler::queries;
				case "/updates" -> handler::updates;
				case "/fortunes" -> handler::fortunes;
				default -> r -> ServerResponse.notFound().build();
			};
			return Mono.just(fn);
		};
	}

}