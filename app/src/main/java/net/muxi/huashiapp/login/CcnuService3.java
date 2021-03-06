package net.muxi.huashiapp.login;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface CcnuService3 {

    @POST("https://account.ccnu.edu.cn/cas/login;jsessionid={jsession}")
    @FormUrlEncoded
    Observable<ResponseBody> performCampusLogin(@Path("jsession") String jsession, @Field("username")
            String usrname, @Field("password") String password
            , @Field("lt") String valueOfLt, @Field("execution") String valueOfExe,
                                                @Field("_eventId") String submit, @Field("submit") String login);

    //教务系统的登录
    //需要携带cookie cookie没有放在header里面
    @GET("https://account.ccnu.edu.cn/cas/login?service=http%3A%2F%2Fxk.ccnu.edu.cn%2Fsso%2Fpziotlogin")
    Observable<ResponseBody> performSystemLogin();


    //account.ccnu.edu.cn 先从这个进行统一身份认证
    @GET("https://account.ccnu.edu.cn/cas/login")
    Observable<Response<ResponseBody>>firstLogin();

    @POST("http://xk.ccnu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005")
    @FormUrlEncoded
    Observable<ResponseBody>getScores(@Field("xnm") String xnm, @Field("xqm") String xqm, @Field("_search") boolean search,
                                      @Field("nd") String nd, @Field("queryModel.showCount") int num, @Field("queryModel.currentPage") int page,
                                      @Field("queryModel.sortName") String sortname, @Field("queryModel.sortOrder") String order, @Field("time") int time);

    //获取平时成绩和期末成绩
    @POST("http://xk.ccnu.edu.cn/jwglxt/cjcx/cjcx_cxCjxq.html?time=1562376341836&gnmkdm=N305005")
    @FormUrlEncoded
    Observable<ResponseBody>getUAE(@Field(("jxb_id"))String jxb_id,@Field("xnm")int xnm,@Field(("xqm"))int xqm,@Field("kcmc")String name);
    @GET("http://202.114.34.15/reader/hwthau.php")
    Observable<ResponseBody>perLibLogin();

}
