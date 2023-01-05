package com.lhj.bookstore.repository.custom;

import java.util.List;

import com.lhj.bookstore.entity.BookInfoEntity;
import static com.lhj.bookstore.entity.QBookInfoEntity.bookInfoEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookInfoRepositoryCustomImpl implements BookInfoRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;

	@Override
	public List<BookInfoEntity> searchBookInfo() {
		// TODO Auto-generated method stub
//		QBookInfoEntity bookInfo = QBookInfoEntity.bookInfoEntity;
		return queryFactory.selectFrom(bookInfoEntity).fetch();
	}

}
