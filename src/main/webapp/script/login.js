var myApp = angular.module('singInApp', [])
    .controller('signController', ['$scope', '$http', function ($scope, $http) {

        $scope.signIn = function () {
            layer.msg('加载中', {
                icon: 16
                , shade: 0.01
            });


            $http.get('manager/signIn.do', {params: {name: $scope.username, password: $scope.pwd}})
                .success(function (data, status) {
                    if (data.code === 1 && status === 200) {

                        // sessionStorage.setItem('username', data.data.name);
                        window.location.pathname += 'home.html';
                    } else if (-200 === data.code) {
                        layer.msg('该用户已被禁用！请联系管理员:(', {icon: 5});
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