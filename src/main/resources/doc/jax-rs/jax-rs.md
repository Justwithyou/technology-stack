# JAX-RS

JAX-RS是JAVA EE6引入的一个新技术，JAX-RS即Java API For RESTFul Web Service，是一个Java语言的应用程序接口，支持按照
表述性状态转移（REST）架构风格创建Web服务

常用注解：

> @Path：标注资源类或者方法的相对路径
>
> @GET、@POST、@PUT、@DELETE：标注方法的HTTP请求类型
>
> @Produces：标注返回的MIME媒体类型
>
> @Consumes：标注可接受请求的MIME媒体类型
>
> @PathParam、@QueryParam、@HeaderParam、@CookieParam、@MatrixParam、@FormParam：标注方法的参数来自于HTTP请求的不同位置

基于JAX-RS实现的框架有Jersey/RESTEasy等，这两个框架创建的应用可以很方便的部署到Servlet容器中

JAX-RS规范中定义的包名：javax.ws.rs