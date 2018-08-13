# DINO-Parking-System-Backend

## 1. 系统角色分类
   1. 系统管理员  `ADMIN`
   2. 经理       `MANAGER`
   3. 停车小弟    `EMPLOYEE`
   4. 普通用户    `USER`

## 2. 系统角色权限 

角色 | 管理用户 | 管理停车场 | 管理停车小弟 | 指派订单 | 抢单 | 停车 | 取车 |
---|----|----|----|----|---|---|---
系统管理员 | √ | √ | √ | √ | | | |
经理 | | √ | √ | √ | | | 
停车小弟 | | | | | √ | √ | √ |
普通用户 | | | | | | | | |


## 3. API

### 1. 停车场管理 /parkingLots

1. 获取所有停车信息
    - path：`/parkingLots`
    - method：GET
    - response：
        ```json
        [
          {
             "id": 1,
             "name": "停车场A",
             "size": 20,
             "status": "normal"
          }
        ]
    

2. 添加一个停车场
    - path：`/parkingLots`
    - method：POST
    - request:
      ```json
      {
        "name":"oocl停车场",
        "size":20
      }
    - response：
        - 成功：201
        - 失败：400

3. 修改停车场信息
    - path：`/parkingLots/:id`
    - method: PUT
    - request:
        ```json
        [
            {
                "name":"XX lot",
                "size":12
            }
        ]
    - response:
        - 成功： 201
        - 失败： 400
4. 注销/开放停车场
    - path：`/parkingLots/:id`
    - method: PATCH
    - response
        - 成功： 200
        - 失败： 400

3. 获取所有停车场dashboard数据
    - path：`/dashboard`
    - method: GET
    - response:
        ```json
        [
                {
                    "parkingLotId": 1,
                    "parkingLotName": "停车场A",
                    "size": 20,
                    "carNum": 0,
                    "status":"normal",
                    "parkingBoyId": 0,
                    "parkingBoyName": null
                }
        ]


4. 分页获取所有停车场dashboard数据
    - path：`/dashboard/page/:page/pageSize/:size`
    - method: GET
    - response:
        ```json
        [
              {
                  "parkingLotId": 1,
                  "parkingLotName": "停车场A",
                  "size": 20,
                  "carNum": 0,
                  "status":"normal",
                  "parkingBoyId": 0,
                  "parkingBoyName": null
              }
        ]

5. 获取所有目前未被管理的停车场信息
      - path：` /parkingLots/?noParkingBoy=true`
      - method: GET
      - response:
        ```json
        [
            {
                "id": 10,
                "name": "oocl停车场10",
                "size": 10,
                "carNum": 0,
                "status": true
            }
        ]
        ```

6. 停车场信息组合查询
    - path：`/parkingLots?name=:name&&eq=:size&&gt=:left&&lt=:right`
    - path：`/parkingLots?id=:id`
    - method: GET
    - response:
        ```json
            [
                   {
                        "id": 1,
                        "name": "oocl停车场1",
                        "size": 20,
                        "carNum": 13,
                        "status": true
                    }
            ]
        ```
       
### 2. 订单管理

1. 获取所有order
    - path：`/orders`
    - method: GET
    - response:
        ```json
        [
           {
               "id": 1,
               "type": "parkOutCar",
               "parkingBoy": {
                   "id": 2,
                   "username": "parkingboy1",
                   "workStatus": "onduty"
               },
               "plateNumber": "粤DHC9767",
               "status": "handle",
               "receiptId": "1"
           },
           {
               "id": 2,
               "type": "parkCar",
               "parkingBoy": null,
               "plateNumber": "粤DH76647",
               "status": "nohandle",
               "receiptId": "2"
           }
        ]
2. 组合查询order
    - path：`/orders?type=:type&&plateNumber=:plateNumber&&status=:status`(type和status非模糊查询，下拉选择指定值)
    - path：`/orders?id=:id`
    - method: GET
    - response:
        ```json
        [
           {
               "id": 6,
               "type": "parkCar",
               "parkingLotName": null,
               "parkingBoy": null,
               "plateNumber": "粤VH71647",
               "status": "noRob",
               "receiptId": "6",
               "parkDate": "10:24 - 2018/08/05",
               "unParkDate": null
           },
           {
               "id": 7,
               "type": "parkOutCar",
               "parkingLotName": "oocl停车场2",
               "parkingBoy": {
                   "id": 2,
                   "nickname": "停车小弟A",
                   "username": "parkingboy1",
                   "phone": "13160675789",
                   "workStatus": "offduty",
                   "status": true
               },
               "plateNumber": "粤C32412",
               "status": "waitUnPark",
               "receiptId": "7",
               "parkDate": "10:24 - 2018/08/05",
               "unParkDate": null
           }
        ]
3. 根据状态获取order
    - path：`/orders/:status`
    - method: GET
    - request:
        ```json
        [
               {
                   "id": 1,
                   "type": "parkCar",
                   "plateNumber": "粤DHC9767",
                   "status": "nohandle",
                   "receiptId": "1"
               },
               {
                   "id": 2,
                   "type": "parkCar",
                   "plateNumber": "粤DH76647",
                   "status": "nohandle",
                   "receiptId": "2"
               }
           ]
  

4. 小弟抢单
    - path：`/orders/:id`
    - method: PUT
    - request:
        ```json
        {
            "status":"waitPark",
            "parkingBoyId":2
        }
4. 经理指派订单给小弟
    - path：`/orders/:id?appoint=true`
    - method: PUT
    - request:
        ```json
        {
            "status":"waitPark",
            "parkingBoyId":2
        }
   
5. parkingBoy完成取车订单
    - path：`/orders/:id`
    - method: PUT
    - request:
        ```json
        {
            "status":"finish",
            "parkingBoyId":2
        }
  
 6. 修改订单的阅读状态
     - path：`/orders/:id`
     - method: PATCH
     - request:
         ```json
         {
             "parkingBoyId":2
         }
  
### 3. 停车小弟管理

1. 停车小弟选择停车场停车
    - path:`/parkingBoys/{parkingBoyId}/parkingLots/{parkingLotId}`
    - method:PUT
    - request:
        ```json
        {
          "orderId":1
        }
    - response:
        - 成功：200 OK
        - 失败：400 Bad Requset

2. 获取所有小弟信息列表
    - path:`/parkingBoys`
    - method:GET
    - response:
        - 成功：200 OK
        ```json
        [
                {
                    "nickname": "停车小弟A",
                    "id": 2,
                    "phone": "13160675789",
                    "email": "120@qq.com",
                    "workStatus": "onduty",
                    "lotNumber": 2,
                    "carNumber": 23,
                    "total": 30
                },
                {
                    "nickname": "停车小弟B",
                    "id": 4,
                    "phone": "13160675789",
                    "email": "121@qq.com",
                    "workStatus": "onduty",
                    "lotNumber": 2,
                    "carNumber": 1,
                    "total": 45
                }
            ]
        ```
        - 失败：400 Bad Requset
        
3. 获取历史（已完成）订单
    - path:`/parkingBoys/:parkingBoyId/historyOrders`
    - method:GET
    - response:
      ```json
        [
                {
                        "id": 7,
                        "type": "parkOutCar",
                        "parkingBoy": {
                            "id": 2,
                            "nickname": "停车小弟A",
                            "username": "parkingboy1",
                            "workStatus": "onduty"
                        },
                        "plateNumber": "粤C32412",
                        "status": "finish",
                        "receiptId": "7",
                        "parkDate": "09:10 - 2018/08/03",
                        "unParkDate": "09:10 - 2018/08/03"
                    }
          ]
       ```
       
4. 获取小弟所有未满的停车场
    - path:`/parkingBoys/:id/noFullParkingLots`
    - method:GET
    - response:
      ```json
        [
            {
                "id": 3,
                "name": "oocl停车场3",
                "size": 20,
                "carNum": 1,
                "status": true
            },
            {
                "id": 4,
                "name": "oocl停车场4",
                "size": 25,
                "carNum": 0,
                "status": true
            }
        ]
       ```
5. 获取小弟的所有订单
    - path：` /parkingBoys/:id/noHandleOrders`
    - method: GET
    - response:
        ```json
        [
           {
               "id": 1,
               "type": "parkOutCar",
               "parkingBoy": {
                   "id": 2,
                   "username": "parkingboy1",
                   "workStatus": "onduty"
               },
               "plateNumber": "粤DHC9767",
               "status": "handle",
               "receiptId": "1"
           },
           {
               "id": 2,
               "type": "parkCar",
               "parkingBoy": null,
               "plateNumber": "粤DH76647",
               "status": "nohandle",
               "receiptId": "2"
           }
        ]
 
 6. 获取小弟目前管理的停车场
       - path：` /parkingBoys/:id/parkingLots`
       - method: GET
       - response:
         ```json
         [
             {
                 "id": 8,
                 "name": "oocl停车场8",
                 "size": 10,
                 "carNum": 0,
                 "status": true
             }
         ]

 7. 经理分配小弟的停车场
       - path：` /parkingBoys/:id/parkingLots`
       - method: PUT
       - request:
         ```json
         {
         	"operation":"add",//"remove"
         	"parkingLots":[1,2,3]
         }
         ```
       - respose:
           - 成功：200
           - 失败：400
  ### 4. 用户管理
  1. 用户授权(ROLE_ADMIN才可以使用)
      - path：` /users/:id/roles`
      - method: PUT
      - request:
        ```json
        {
        	"role":"ROLE_ADMIN" // ROLE_MANAGER,ROLE_PARKINGBOY
        }
        ```
      - response:
          - 成功：200
          - 失败：400
        
 2. 用户工作状态的修改
      - path：` /users/:id/workStatus`
      - method: PUT
      - request:
         ```json
         {
         	"workStatus":"onduty"
 	        // onduty 上班
            // offduty 下班
            // late 迟到
            // leave 请假
         }
         ```
      - response:
                - 成功：200
                - 失败：400
 3. 用户(停车小弟)组合查询
      - path：`/users?username=:username&&nickname=:nickname&&status=:status&&workStatus&&email=:email&&phone=:phone`
      - path：`/users?id=:id`
      - method: GET
      - response:
          ```json
              [
                 {
                      "id": 2,
                      "username": "parkingboy1",
                      "nickname": "停车小弟A",
                      "email": "120@qq.com",
                      "phone": "13160675789",
                      "workStatus": "offduty",
                      "status": true
                  }
              ]
          ```
4. 用户打卡
     - path：`/users/:id/workStatus`
     - method：PATCH

5. 冻结用户
     - path:`/users/:id`
     - method: PATCH
     - request:
        ```json
        {
        	"status":false // false 冻结，true 解冻
        }
        ```
     - response:
        - 成功 204
        - 失败：400
        ```json
        {
            "result": "failed",
            "cause": "该停车员手下还有管理的停车场" //"管理员账号不能冻结"
        }
        ```