package com.github.emailtohl.building.site.service.impl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;

import com.github.emailtohl.building.bootspring.SpringUtils;
import com.github.emailtohl.building.common.jpa.Pager;
import com.github.emailtohl.building.site.dao.SearchResult;
import com.github.emailtohl.building.site.dao.UserRepository;
import com.github.emailtohl.building.site.dto.ForumPostDto;
import com.github.emailtohl.building.site.service.ForumPostService;
/**
 * 全文搜索测试
 * 先存储论坛帖子，然后再通过索引查询
 * 
 * @author HeLei
 */
public class ForumPostServiceTest {
	static final Logger logger = LogManager.getLogger();
	AnnotationConfigApplicationContext ctx = SpringUtils.context;
	ForumPostService forumPostService = ctx.getBean(ForumPostService.class);
	UserRepository userRepository = ctx.getBean(UserRepository.class);
	ForumPostDto p1 = new ForumPostDto(), p2 = new ForumPostDto(), p3 = new ForumPostDto();
	static String TITLE_EMAILTOHL = "emailtohl's post", TITLE_FOO = "foo's post", TITLE_BAR = "bar's post";
	@Before
	public void setUp() throws Exception {
		p1.setTitle(TITLE_EMAILTOHL);
		p1.setKeywords("first emailtohl");
		p1.setBody("hl's first post hello forum");
		
		p2.setTitle(TITLE_FOO);
		p2.setKeywords("first foo");
		p2.setBody("foo's first post hello forum");
		
		p3.setTitle(TITLE_BAR);
		p3.setKeywords("first bar");
		p3.setBody("bar's first post hello forum");
		
		forumPostService.save("emailtohl@163.com", p1);
		forumPostService.save("foo@test.com", p2);
		forumPostService.save("bar@test.com", p3);
	}

	@Test
	public void testSearch() {
		Pager<SearchResult<ForumPostDto>> p = forumPostService.search("first", new PageRequest(0, 20));
		List<String> ls = Arrays.asList(TITLE_EMAILTOHL, TITLE_FOO, TITLE_BAR);
		for (SearchResult<ForumPostDto> s : p.getContent()) {
			System.out.println(s.getEntity().getTitle());
			System.out.println(s.getEntity().getKeywords());
			System.out.println(s.getEntity().getBody());
			System.out.println(s.getEntity().getUser().getEmail());
			System.out.println(s.getEntity().getUser().getName());
			System.out.println(s.getRelevance());
			assertTrue(ls.contains(s.getEntity().getTitle()));
		}
		
		p = forumPostService.search("emailtohl@163.com", new PageRequest(0, 20));
		for (SearchResult<ForumPostDto> s : p.getContent()) {
			System.out.println(s.getEntity().getTitle());
			System.out.println(s.getEntity().getKeywords());
			System.out.println(s.getEntity().getBody());
			System.out.println(s.getEntity().getUser().getEmail());
			System.out.println(s.getEntity().getUser().getName());
			System.out.println(s.getRelevance());
			assertEquals(TITLE_EMAILTOHL, s.getEntity().getTitle());
		}
	}

	@After
	public void clean() {
		forumPostService.delete(forumPostService.getForumPostByTitle(TITLE_EMAILTOHL).getId());
		forumPostService.delete(forumPostService.getForumPostByTitle(TITLE_FOO).getId());
		forumPostService.delete(forumPostService.getForumPostByTitle(TITLE_BAR).getId());
	}
}