package com.tts.techtalentblog.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tts.techtalentblog.models.BlogPost;
import com.tts.techtalentblog.repositories.BlogPostRepository;

// We don't give this RestController because we want to return links to HMTL
// rather than ResponseBodies / JSON
@Controller
public class BlogPostController {
	
	@Autowired
	BlogPostRepository blogPostRepository;
	
	@GetMapping("/")
	public String home(Model model) {
		List<BlogPost> blogPosts = blogPostRepository.findAll();
		model.addAttribute("blogPosts", blogPosts);
				
		return "blogPost/index.html";
	}
	
	@GetMapping("/new")
	public String newPost(Model model) {
		BlogPost blogPost = new BlogPost();
		
		model.addAttribute("blogPost", blogPost);
		
		return "blogPost/new.html";
	}

	@PostMapping("/blogposts")
	public String createNewBlogPost(BlogPost blogPost, Model model) {		
		BlogPost dbBlogPost = blogPostRepository.save(blogPost);
		
		model.addAttribute("newBlogPost", dbBlogPost);
		
		return "blogPost/results.html";
	}
	
	@GetMapping("/blogposts/{id}")
	public String editPostWithId(@PathVariable(value="id") Long id, Model model) {
		Optional<BlogPost> blogPost = blogPostRepository.findById(id);		
		
		if (blogPost.isPresent()) {
			// removes optional 
			BlogPost blogPostById = blogPost.get();
			model.addAttribute("blogPost", blogPostById);
		}
		
		return "blogPost/edit.html";
	}
	
	@PatchMapping("/blogposts/{id}")
	public String updatePost(@PathVariable(value="id") Long id, BlogPost blogPost, Model model) {
		Optional<BlogPost> blogPostById = blogPostRepository.findById(id);
		
		if (blogPostById.isPresent()) {
			BlogPost dbBlogPost = blogPostById.get();
			
			dbBlogPost.setTitle(blogPost.getTitle());
			dbBlogPost.setAuthor(blogPost.getAuthor());
			dbBlogPost.setBlogEntry(blogPost.getBlogEntry());
			
			blogPostRepository.save(dbBlogPost);
			
			model.addAttribute("newBlogPost", dbBlogPost);
		}
		
		return "blogPost/results.html";
	}
	
	@DeleteMapping("/blogposts/{id}")
	public String deletePostById(@PathVariable(value="id") Long id, Model model) {
		
		Optional<BlogPost> blogPostById = blogPostRepository.findById(id);
		
		if (blogPostById.isPresent()) {
			BlogPost dbBlogPost = blogPostById.get();
			model.addAttribute("deleteBlogPost", dbBlogPost);
			blogPostRepository.deleteById(id);
		}
		
		return "blogPost/delete.html";
	}
}
