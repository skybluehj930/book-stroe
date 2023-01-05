package com.lhj.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.repository.custom.BookInfoRepositoryCustom;

@Repository
public interface BookInfoRepository extends JpaRepository<BookInfoEntity, Long>, BookInfoRepositoryCustom {

	public List<BookInfoEntity> findByTitleContaining(String title);

}
