/**
 * 正则验证
 * @param str
 * @param expression
 */
function regular(str, expression) {
    return expression.test(str);
}

const regularExpression = {
    //用户名
    userName: '^[a-zA-Z][a-zA-Z0-9_]{6,12}$',
    //密码
    userPass: '^[a-zA-Z0-9_]{6,12}$',
    //邮箱
    email: '^\w+([-+.]\w+)*@qq.com*$',
    //验证码
    validCode: '^[a-zA-Z0-9]{6}$'
};