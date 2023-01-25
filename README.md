# Querydsl과 H2db를 활용한 Spring REST Api JUnit5 Test
<br/>

## ERD
<img src="https://github.com/skybluehj930/book-stroe/blob/master/src/main/resources/static/readme-img/bookstore-erd.png" width="700" alt="bookstore-erd"></img>  

* 다대다 구조를 연관관계 테이블을 활용해 일대다, 다대일 구조로 풀어내고  
양방향 연관관계 저장, 조회 삭제를 jpa와 Querydsl로 구현했습니다.
<br/><br/>

## 요구사항 정리
* 공통 사항 
  * 목록 조회는 페이징을 적용
* 도서 정보 등록, 검색, 수정 API
  * 도서구분 type이 일치하고 검색 keyword가 도서 제목 or 저자명에 포함 하는 필터링 검색 API
* 공급 도서 조회 API  
  * 공급 내역을 포함하여 공급된 도서 제목, 저자, 구분으로 필터링 검색 API
  * 공급 내역을 삭제하는 API
* 계약업체 등록, 검색, 내용 변경 API  
  * 계약업체별 공급된 도서를 계약상태코드, 도서 제목, 저자, 구분으로 필터링 검색 API

<br/>

## Swagger
<img src="https://github.com/skybluehj930/book-stroe/blob/master/src/main/resources/static/readme-img/bookstore-swagger.png" alt="bookstore-swagger"></img>
