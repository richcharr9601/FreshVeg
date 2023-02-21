package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import com.example.demo.repository.RepositoryWrapper;

@SpringBootTest
class RepositoryWrapperTests {

	@TestConfiguration
	public static class RepositoryWrapperTestsConfiguration {
		@Bean
		RepositoryWrapper repositoryWrapper() {
			return new RepositoryWrapper();
		}
	}

	@Autowired
	RepositoryWrapper repositoryWrapper;

	@Test
	void testGetRepository() {
	}
}
