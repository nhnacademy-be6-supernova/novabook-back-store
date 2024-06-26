package store.novabook.store.common.adatper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import store.novabook.store.common.adatper.dto.CreateCouponRequest;
import store.novabook.store.common.adatper.dto.CreateCouponResponse;
import store.novabook.store.common.adatper.dto.GetCouponAllRequest;
import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.response.ApiResponse;

@FeignClient(name = "couponClient")
public interface CouponAdapter {

	@GetMapping
	ApiResponse<GetCouponAllResponse> getCouponAll(@RequestBody GetCouponAllRequest request);

	@PostMapping("/coupons")
	ApiResponse<CreateCouponResponse> createCoupon(@RequestBody CreateCouponRequest request);

	@PutMapping("/{couponId}")
	ApiResponse<Void> useCoupon(@PathVariable("couponId") Long couponId);
}
