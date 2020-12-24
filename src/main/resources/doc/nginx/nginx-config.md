# nginx

nginx官网：[http://nginx.org/]

nginx下载地址：[http://nginx.org/en/download.html]

nginx中文文档：[https://www.nginx.cn/doc/index.html]


## nginx location配置

语法规则：location [=|~|~*|^~] /uri/ {...}

> '='开头标识精确匹配
>
> '^~'开头表示uri以某个常规字符串开头，理解为配置url路径即可，XX开头
>
> '~' 开头表示区分大小写的正则匹配，XX结尾
>
> '~*'开头表示不区分大小写的正则匹配，XX结尾
>
> '!~'和'!~*'分别为区分大小写不匹配及不区分大小写不匹配的正则
>
> '/'通用匹配
