var navs = [
    {
        "title": "后台管理",
        "icon": "fa-cubes",
        "spread": true,
        "href": "/admin/index",
    },
    {
        "title": "商品管理",
        "icon": "fa-wpforms",
        "spread": true,
        "children": [
            {
                "title": "商品列表",
                "icon": "fa-angle-right",
                "href": "/admin/product"
            },
            {
                "title": "活动管理",
                "icon": "fa-angle-right",
                "href": "/admin/activity"
            }
        ]
    },

    {
        "title": "订单管理",
        "icon": "fa-wpforms",
        "spread": true,
        "children": [
            {
                "title": "订单列表",
                "icon": "fa-angle-right",
                "href": "/admin/orders"
            },
            {
                "title": "提货列表",
                "icon": "fa-angle-right",
                "href": "/admin/"
            }
        ]
    }

];