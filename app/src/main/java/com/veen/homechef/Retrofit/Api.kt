package com.veen.donorsystem.api

import com.veen.homechef.Model.HomePage.menuitem.MenuitemResponse
import com.veen.homechef.Model.Cart.AddCart.AddCartRequest
import com.veen.homechef.Model.Cart.AddCart.AddCartResponse
import com.veen.homechef.Model.Cart.CartView.CartViewRequest
import com.veen.homechef.Model.Cart.CartView.CartViewResponse
import com.veen.homechef.Model.Cart.delete.DeleteCartRequest
import com.veen.homechef.Model.Cart.delete.DeleteCartResponse
import com.veen.homechef.Model.Cart.update.CartUpdateRequest
import com.veen.homechef.Model.Cart.update.CartUpdateResponse
import com.veen.homechef.Model.HomePage.dashboard.DashboardRes
import com.veen.homechef.Model.HomePage.menu.MenuRequest
import com.veen.homechef.Model.HomePage.menu.MenuResponse
import com.veen.homechef.Model.HomePage.menuitem.MenuItemRequest
import com.veen.homechef.Model.HomePage.subcategory.MenuSubRequest
import com.veen.homechef.Model.HomePage.subcategory.MenuSubResponse
import com.veen.homechef.Model.Profile.ChangePass.ChangePassRequest
import com.veen.homechef.Model.Profile.ChangePass.ChangePassResponse
import com.veen.homechef.Model.Login.LoginRequest
import com.veen.homechef.Model.Login.LoginResponse
import com.veen.homechef.Model.Login.Relogin.ReloginReq
import com.veen.homechef.Model.Login.Relogin.ReloginRes
import com.veen.homechef.Model.Profile.giftcarddetails.GiftCardDetailsRequest
import com.veen.homechef.Model.Profile.giftcarddetails.GiftCardDetailsResponse
import com.veen.homechef.Model.Profile.myorder.MyOrderRequest
import com.veen.homechef.Model.Profile.myorder.MyOrderResponse
import com.veen.homechef.Model.Profile.updateprofile.UpdateProfileRequest
import com.veen.homechef.Model.Profile.updateprofile.UpdateProfileResponse
import com.veen.homechef.Model.Profile.uploadimage.UploadImageRequest
import com.veen.homechef.Model.Profile.uploadimage.UploadImageResponse
import com.veen.homechef.Model.Profile.viewdetails.ViewDetailsRequest
import com.veen.homechef.Model.Profile.viewdetails.ViewDetailsResponse
import com.veen.homechef.Model.Profile.viewprofile.ViewProfileRequest
import com.veen.homechef.Model.Profile.viewprofile.ViewProfileResponse
import com.veen.homechef.Model.Search.SearchRequest
import com.veen.homechef.Model.Search.SearchResponse
import com.veen.homechef.Model.SignUp.SignUpRequest
import com.veen.homechef.Model.SignUp.SignUpResponse
import com.veen.homechef.Model.cancelorder.CancelOrderReq
import com.veen.homechef.Model.cancelorder.CancelOrderRes
import com.veen.homechef.Model.chef.ChefReq
import com.veen.homechef.Model.chef.ChefRes
import com.veen.homechef.Model.coupon.Fetch.FetchCouponRequest
import com.veen.homechef.Model.coupon.Fetch.FetchCouponResponse
import com.veen.homechef.Model.coupon.apply.ApplyCouponRequest
import com.veen.homechef.Model.coupon.apply.ApplyCouponResponse
import com.veen.homechef.Model.coupon.remove.CouponRemovedRequest
import com.veen.homechef.Model.coupon.remove.CouponRemovedResponse
import com.veen.homechef.Model.itemdetails.CheckoutItemRequest
import com.veen.homechef.Model.itemdetails.CheckoutItemResponse
import com.veen.homechef.Model.location.city.CityRequest
import com.veen.homechef.Model.location.city.CityResponse
import com.veen.homechef.Model.location.country.CountryResponse
import com.veen.homechef.Model.location.state.StateRequest
import com.veen.homechef.Model.location.state.StateResponse
import com.veen.homechef.Model.order.OrderRequest
import com.veen.homechef.Model.order.OrderResponse
import com.veen.homechef.Model.rating.RatingReq
import com.veen.homechef.Model.rating.RatingRes
import retrofit2.Call
import retrofit2.http.*

interface Api {
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(@Body login: LoginRequest?): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("registration")
    fun signup(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @Headers("Content-Type: application/json")
    @POST("change_password")
    fun changepass(@Header("Authorization")token:String,
                      @Body changePassRequest: ChangePassRequest
    ): Call<ChangePassResponse>

    @Headers("Content-Type: application/json")
    @POST("my_order")
    fun myorder(@Header("Authorization")token:String,
                   @Body myOrderRequest: MyOrderRequest
    ): Call<MyOrderResponse>

    @Headers("Content-Type: application/json")
    @POST("profile")
    fun profileview(@Header("Authorization")token:String,
                @Body viewProfileRequest: ViewProfileRequest
    ): Call<ViewProfileResponse>

    @Headers("Content-Type: application/json")
    @POST("update_profile")
    fun profileupdate(@Header("Authorization")token:String,
                    @Body updateProfileRequest: UpdateProfileRequest
    ): Call<UpdateProfileResponse>

    @Headers("Content-Type: application/json")
    @POST("country")
    fun countryupdate(@Header("Authorization")token:String
    ): Call<CountryResponse>

    @Headers("Content-Type: application/json")
    @POST("state")
    fun stateupdate(@Header("Authorization")token:String,
                      @Body stateRequest: StateRequest
    ): Call<StateResponse>

    @Headers("Content-Type: application/json")
    @POST("city")
    fun cityupdate(@Header("Authorization")token:String,
                    @Body cityRequest: CityRequest
    ): Call<CityResponse>

    @Headers("Content-Type: application/json")
    @POST("view_order_detail")
    fun vieworderdetails(@Header("Authorization")token:String,
                   @Body viewDetailsRequest: ViewDetailsRequest
    ): Call<ViewDetailsResponse>

    @Headers("Content-Type: application/json")
    @POST("gift_cards_details")
    fun giftcards(
        @Header("Authorization")token:String,
        @Body giftCardDetailsRequest: GiftCardDetailsRequest
    ): Call<GiftCardDetailsResponse>

    @Headers("Content-Type: application/json")
    @POST("our_menu")
    fun menu(
        @Body menuRequest: MenuRequest
    ): Call<MenuResponse>

    @Headers("Content-Type: application/json")
    @POST("our_menu_item")
    fun menuitem(
        @Body menuItemRequest: MenuItemRequest
    ): Call<MenuitemResponse>

    @Headers("Content-Type: application/json")
    @POST("add_to_cart")
    fun addtocart(
        @Body addCartRequest: AddCartRequest
    ): Call<AddCartResponse>

    @Headers("Content-Type: application/json")
    @POST("cart")
    fun cartview(
        @Body cartViewRequest: CartViewRequest
    ): Call<CartViewResponse>

    @Headers("Content-Type: application/json")
    @POST("update_qty")
    fun updatecartview(
        @Body cartUpdateRequest: CartUpdateRequest
    ): Call<CartUpdateResponse>

    @Headers("Content-Type: application/json")
    @POST("delete_item")
    fun deletecart(
        @Body deleteCartRequest: DeleteCartRequest
    ): Call<DeleteCartResponse>

    @Headers("Content-Type: application/json")
    @POST("our_menu_subcategory")
    fun subcategory(
        @Body menuSubRequest: MenuSubRequest
    ): Call<MenuSubResponse>

    @Headers("Content-Type: application/json")
    @POST("order_now")
    fun ordernow(
        @Header("Authorization")token:String,
        @Body orderRequest: OrderRequest
    ): Call<OrderResponse>

    @Headers("Content-Type: application/json")
    @POST("upload_photo")
    fun uploadimages(
        @Header("Authorization")token:String,
        @Body uploadImageRequest: UploadImageRequest
    ): Call<UploadImageResponse>

    @Headers("Content-Type: application/json")
    @POST("search")
    fun search(
        @Header("Authorization")token:String,
        @Body searchRequest: SearchRequest
    ): Call<SearchResponse>

    @Headers("Content-Type: application/json")
    @POST("checkout")
    fun checkoutitem(
        @Header("Authorization")token:String,
        @Body checkoutItemRequest: CheckoutItemRequest
    ): Call<CheckoutItemResponse>

    @Headers("Content-Type: application/json")
    @POST("fetch_coupon_list")
    fun fetchcoupon(
        @Header("Authorization")token:String,
        @Body fetchCouponRequest: FetchCouponRequest
    ): Call<FetchCouponResponse>

    @Headers("Content-Type: application/json")
    @POST("apply_coupon")
    fun applycoupon(
        @Header("Authorization")token:String,
        @Body applyCouponRequest: ApplyCouponRequest
    ): Call<ApplyCouponResponse>

    @Headers("Content-Type: application/json")
    @POST("remove_coupon")
    fun removecoupon(
        @Header("Authorization")token:String,
        @Body couponRemovedRequest: CouponRemovedRequest
    ): Call<CouponRemovedResponse>

    @Headers("Content-Type: application/json")
    @POST("cancel_order")
    fun cancelorder(
        @Header("Authorization")token:String,
        @Body cancelOrderReq: CancelOrderReq
    ): Call<CancelOrderRes>

    @Headers("Content-Type: application/json")
    @GET("get_home")
    fun gethome(): Call<DashboardRes>

    @Headers("Content-Type: application/json")
    @POST("get_order_food_chef")
    fun chefID(
        @Header("Authorization")token:String,
        @Body chefReq: ChefReq
    ): Call<ChefRes>

    @Headers("Content-Type: application/json")
    @POST("rating")
    fun ratingchef(
        @Header("Authorization")token:String,
        @Body ratingReq: RatingReq
    ): Call<RatingRes>

    @Headers("Content-Type: application/json")
    @POST("update_cart_afterlogin")
    fun relogin(
        @Header("Authorization")token:String,
        @Body reloginReq: ReloginReq
    ): Call<ReloginRes>

//    @Headers("Content-Type: application/json")
//    @GET("get_home")
//    fun gethome(): Call<>

}