# Java Web Application

- **Build**: `mvn clean package`
- **Start**: `java -jar was.jar`
- **Logs**: `{프로젝트 루트 경로}/logs`




## 설정
resources 의 `application.json` 파일을 통해 간단한 was 설정을 할 수 있습니다.

- server.port : 실행할 앱의 port 번호
- server.hosts : host별 설정
  - name: host 명
  - root: 루트 경로( / ) 접근시 매핑되는 경로
  - mapping: 매핑할 서블릿 설정
    - error: 에러상태별 매핑할 서블릿
    - path: URL 별 매핑할 서블릿


---