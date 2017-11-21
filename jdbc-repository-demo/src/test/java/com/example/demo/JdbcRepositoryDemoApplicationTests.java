package com.example.demo;

import com.example.demo.dao.BookDao;
import com.example.demo.domain.Book;
import com.example.demo.querydsl.QBook;
import com.querydsl.core.types.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcRepositoryDemoApplicationTests {

	@Autowired
	BookDao bookDao;

	@Test
	public void testDao() {
		Book book = new Book();
		book.setBookId(3L);
		book.setTitle("hello");
		bookDao.save(book);
	}

	@Test
	public void testQuery(){
		Predicate predicate = QBook.qBook.title.contains("h");
		System.out.println(bookDao.findAll(predicate));
	}

}
