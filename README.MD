## Integration

> Note that in order to use the store and checkout function, the user must login first.
> Follow the guide below for each functions provided by the SDK:



*  **Login**

Call ```MySabaySDK.getInstance().showLoginView();``` to open login screen

* **Show user profile**

```android

    MySabaySDK.getInstance().getUserInfo(new UserInfoListener() {
                                    @Override
                                    public void userInfo(String info) {
                                        // handle user infomation here
                                    }
                                });
```

* **Logout**

Call ```MySabaySDK.getInstance().logout();```

*  **Store and checkout**

Call ```MySabaySDK.getInstance().showShopView();```
