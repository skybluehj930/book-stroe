package com.lhj.bookstore.controller;

import javax.transaction.Transactional;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTestCommon {
	public static StopWatch stopWatch = new StopWatch();
}
