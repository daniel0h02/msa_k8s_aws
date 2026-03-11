

*모든지 그 파일에 경로에서 해야 함*
> cd xxxxx
run 눌러도 되지만 명령어 치는 연습을 하는 것이 좋음

1. config  server 기동이 먼저 이루어저야 한다
> cd configserver
> ./gradlew bootrun

다만, cloud bus를 사용하게 된다면 config  server 기동 전에 Docker를 이용해서 RabbitMQ가 기동 되어야 함

2. eureka server 기동
코드는 가지고 있지 않다
배포가 되어야 하기 때문에 명령어를 쓴다
> cd eureka
> ./gradlew bootJar
> java -jar build/libs/xxxxx.jar

3. apigateway 기동
> cd appigateway
> ./gradlew bootJar
> java -jar xxxxx.jar



4. 각 서비스 객체가 기동(user, product, order)
> cd user, product, order
> ./gradlew bootJar
> java -jar xxxxx.jar

ps) 특이사항
order 기동시 Kafka(비동기 통신) - Kafka 단독실행 못해서 도움을 받아야함 : zookeeper - docker-compose
> docker-compose up

endpoint)
http://localhost:port/user-sevice/user/signIn
http://localhost:port/product-sevice/product/create
http://localhost:port/order-sevice/order/create
localhost에 우리가 정의한 도메인 넣으면 됨
