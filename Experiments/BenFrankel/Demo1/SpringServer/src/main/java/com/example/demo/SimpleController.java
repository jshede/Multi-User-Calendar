package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.SampleEntity;
import com.example.demo.SampleEntityRepository;

@Controller
public class SimpleController {
	
	@Autowired
	private SampleEntityRepository sampleEntityRepository;

	@GetMapping("/")
	public String index(Model model)
	{
		return "index"; // <-- tells app which .html file to return
	}
	
	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model)
	{
		model.addAttribute("name", name);
		return "greeting";
	}
	
	@GetMapping("/adder")
	public String add(@RequestParam(name="a", required=false) int a, 
			@RequestParam(name="b", required=false) int b, 
			Model model)
	{
		int sum = a + b;
		model.addAttribute("sum", sum);
		return "adder";
	}
	
	@PostMapping(path="/add")
	public @ResponseBody String addSampleEntity(@RequestParam(name="Data") String Data)
	{
		SampleEntity s = new SampleEntity();
		s.setData(Data);
		sampleEntityRepository.save(s);
		return "Saved";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<SampleEntity> getAllSampleEntities(Model model)
	{
		Iterable<SampleEntity> all = sampleEntityRepository.findAll();
		return all;
	}
	
	@ModelAttribute("entities")
	public List<SampleEntity> populateEntities(Model model) {
		List<SampleEntity> list = new ArrayList<>();
		for (SampleEntity e : sampleEntityRepository.findAll()) {
			list.add(e);
		}
		return list;
	}
}
