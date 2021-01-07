//package com.technology.stack.graphql;
//
//import com.google.common.base.Charsets;
//import com.google.common.io.Resources;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.net.URL;
//
///**
// * TODO
// *
// * @author zhoujunhui-a
// * @version 1.0.0
// * @date 2021/1/7 16:06
// */
//@Component
//public class GraphQLProvider {
//
//    @Autowired
//    GraphQLDataFetchers graphQLDataFetchers;
//    private GraphQL graphQL;
//    @PostConstruct
//    public void init() throws IOException {
//        URL url = Resources.getResource("graphql/schema.graphqls");
//        String sdl = Resources.toString(url, Charsets.UTF_8);
//        GraphQLSchema graphQLSchema = buildSchema(sdl);
//        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
//    }
//    private GraphQLSchema buildSchema(String sdl) {
//        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
//        RuntimeWiring runtimeWiring = buildWiring();
//        SchemaGenerator schemaGenerator = new SchemaGenerator();
//        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
//    }
//    private RuntimeWiring buildWiring() {
//        return RuntimeWiring.newRuntimeWiring()
//                // 仅仅是体验Mutation这个功能,返回一个字符串
//                .type("Mutation", builder -> builder.dataFetcher("hello", new StaticDataFetcher("Mutation hello world")))
//                // 返回字符串
//                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("Query hello world")))
//                // 通过id查询book
//                .type(newTypeWiring("Query").dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
//                // 查询所有的book
//                .type(newTypeWiring("Query").dataFetcher("books", graphQLDataFetchers.getAllBooks()))
//                // 查询book中的author信息
//                .type(newTypeWiring("Book").dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
//                .build();
//    }
//
//    // 执行GraphQL语言的入口
//    @Bean
//    public GraphQL graphQL() {
//        return graphQL;
//    }
//}
