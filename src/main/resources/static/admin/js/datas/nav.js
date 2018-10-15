var navs = [
    {
        "title": "后台管理",
        "icon": "fa-cubes",
        "spread": true,
        "href": "/admin/index",
    },
    {
        "title": "商户管理",
        "icon": "fa-wpforms",
        "spread": true,
        "children": [
            {
                "title": "商户列表",
                "icon": "fa-angle-right",
                "href": "/admin/merchant/index"
            },
            {
                "title": "商品列表",
                "icon": "fa-angle-right",
                "href": "/admin/merchantgoods"
            },
            {
                "title": "商户订单",
                "icon": "fa-angle-right",
                "href": "/admin/merchantorders"
            },
            {
                "title": "问题订单",
                "icon": "fa-angle-right",
                "href": "/admin/merchantrecevieds"
            }
        ]
    },

    {
        "title": "财务管理",
        "icon": "fa-wpforms",
        "spread": true,
        "children": [
            {
                "title": "充值订单",
                "icon": "fa-angle-right",
                "href": "/admin/rechargeorders"
            }
        ]
    },
    {
        "title": "系统管理",
        "icon": "fa-wpforms",
        "spread": true,
        "children": [
            {
                "title": "通知列表",
                "icon": "fa-angle-right",
                "href": "/admin/merchantnotifys"
            },
            {
                "title": "APP管理",
                "icon": "fa-angle-right",
                "href": "/admin/app/index"
            }
        ]
    }

];