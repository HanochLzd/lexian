var myApp = angular.module('singInApp', [])
    .controller('signController', ['$scope', '$http', function ($scope, $http) {

        $scope.signIn = function () {
            layer.msg('加载中', {
                icon: 16
                , shade: 0.01
            });

            let config = {
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function (obj) {
                    let str = [];
                    for (let s in obj) {
                        str.push(encodeURIComponent(s) + "=" + encodeURIComponent(obj[s]));
                    }
                    return str.join("&");
                }
            };
            $http.post('manager/signIn.do', {name: $scope.username, password: $scope.pwd}, config)
                .success(function (data, status) {
                    if (data.code === 1 && status === 200) {

                        // sessionStorage.setItem('username', data.data.name);
                        window.location.pathname += 'home.html';
                    } else if (-200 === data.code) {
                        layer.msg('该用户已被禁用！请联系管理员:(', {icon: 5});
                    } else if (-7 === data.code) {
                        layer.msg('登录失败', {icon: 5});
                    } else {
                        layer.msg('登录异常', {icon: 5});
                    }
                    $scope.pwd = '';
                })
                .error(function (data, status) {
                    layer.msg('登录异常', {icon: 5});
                });
        };

        $scope.clickEnter = function ($events) {
            if ($events.keyCode === 13) {
                $scope.signIn();
            }
        }
    }]);