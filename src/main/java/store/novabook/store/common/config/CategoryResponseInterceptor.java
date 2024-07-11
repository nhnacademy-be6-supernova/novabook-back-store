package store.novabook.store.common.config;

import java.io.PrintWriter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CategoryResponseInterceptor implements HandlerInterceptor {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		// 요청 처리 전에 수행할 작업을 여기에 추가할 수 있습니다.
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
		ModelAndView modelAndView) throws Exception {
		// 요청 처리 후, 뷰를 렌더링하기 전에 수행할 작업을 여기에 추가할 수 있습니다.
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
		Exception ex) throws Exception {
		if ("/api/v1/store/categories".equals(request.getRequestURI()) && "GET".equalsIgnoreCase(request.getMethod())) {

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.print("changed");
			out.flush();
		}
	}
}
