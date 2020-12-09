# json格式化工具

## fastjson

## gson

## jackson

jackson是Spring默认的序列化框架，也是SpringBoot内置的序列化框架

常用注解：

* @JacksonAnnotation

此注解是所有Jackson注解的元注解，标识了此注解的注解表明是Jackson注解的一部分

* @JacksonAnnotationsInside

也是一个元注解，可用于组合其他注解实现复杂的需求

* @JacksonInject

该注解用于反序列化时，如果标识在属性上，则表明反序列化时不使用Json串里面的属性，而是使用自定义的注入（慎用）

* @JsonAlias

该注解用于在反序列化时，指定Json别名（对接多方接口时，来源外部的字段可能不一致，在内部系统可能一致，这时可通过该注解指定别名）

* @JsonInclude

> @JsonInclude：接口响应时的字段序列化策略
>
> @JsonInclude(Include.ALWAYS)：默认策略，任何情况下都序列化，和不写注解一样
>
> @JsonInclude(Include.NON_NULL)：常用，如果加了该注解的字段为Null则不再序列化此字段
>
> @JsonInclude(Include.NON_ABSENT)：包含NON_NULL即为Null的时候不再序列化
>
> @JsonInclude(Include.NON_EMPTY)：包含NON_NULL/NON_ABSENT同时还包含如果字段为空也不序列化
>
> @JsonInclude(Include.NON_DEFAULT)：如果字段是默认值则不序列化
>
> @JsonInclude(Include.CUSTOM)：
>
> @JsonInclude(Include.USE_DEFAULT)

* @JsonAnyGetter

* @JsonAnySetter

* @JsonAutoDetect

* @JsonManagedReference

* @JsonBackReference

* JsonIdentityInfo

* @JsonCreator

* @JsonEnumDefaultValue

* @JsonFilter

可实现实体的动态过滤

* @JsonFormat

* @JsonProperty

* @JsonGetter

* @JsonSetter

* @JsonIgnore

* @JsonIgnoreProperties

* @JsonIgnoreType

