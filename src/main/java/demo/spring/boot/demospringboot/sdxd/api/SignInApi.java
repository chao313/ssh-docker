//package demo.spring.boot.demospringboot.sdxd.api;
//
//import org.springframework.stereotype.Component;
//
//import demo.spring.boot.demospringboot.sdxd.common.api.Authentication;
//import demo.spring.boot.demospringboot.sdxd.common.api.IAuthenticationApi;
//import demo.spring.boot.demospringboot.sdxd.common.api.ICredential;
//
//@Authentication(path = "/api/pub/1.0.0/admin/token", credentialType = SignIn.class)
//@Component
//public class SignInApi implements IAuthenticationApi {
//
//    @Reference(version = "1.0.0")
//    private AdminLoginDubboService adminLoginDubboService;
//
//    @Override
//    public RestResponse<Subject> doAuthenticate(ICredential credential) throws ProcessBizException {
//        if (!SignIn.class.isInstance(credential)) {
//            throw new ProcessBizException(UNKNOWN_CREDENTIAL);
//        }
//
//        SignIn signIn = SignIn.class.cast(credential);
//        LoginRequest request = new LoginRequest();
//        request.setRequestId(BillNoUtils.GenerateBillNo());
//        request.setId(signIn.getUser());
//        request.setPassword(signIn.getPassword());
//        DubboResponse<LoginResponse> response = adminLoginDubboService.submitLogin(request);
//        return rest(response,
//                result -> new Subject(result.getId(), false,
//                        $("userName", result.getName(),
//                                "permissions", result.getAdminPermissionsPos(),
//                                "roles", result.getAdminRolesPos())));
//
//    }
//}