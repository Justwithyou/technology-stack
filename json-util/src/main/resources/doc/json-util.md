# json格式化工具

## fastjson

## gson

## jackson

常用注解：

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