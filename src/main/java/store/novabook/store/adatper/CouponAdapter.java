package store.novabook.store.adatper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.novabook.store.adatper.dto.CreateCouponRequest;
import store.novabook.store.adatper.dto.CreateCouponResponse;
import store.novabook.store.adatper.dto.GetCouponAllRequest;
import store.novabook.store.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.response.ApiResponse;

@FeignClient(name = "couponClient", url = "http://localhost:8070/api/v1")
public interface CouponAdapter {

	@GetMapping
	ApiResponse<GetCouponAllResponse> getCouponAll(@RequestBody GetCouponAllRequest request);

	@PostMapping("/coupons")
	ApiResponse<CreateCouponResponse> createCoupon(@RequestBody CreateCouponRequest request);

	@PutMapping("/{couponId}")
	ApiResponse<Void> useCoupon(@PathVariable("couponId") Long couponId);
}
