package org.vsu.rudakov.controller;

import org.vsu.rudakov.annotation.Controller;
import org.vsu.rudakov.annotation.Inject;
import org.vsu.rudakov.annotation.mapping.GetMapping;
import org.vsu.rudakov.annotation.mapping.PathVariable;
import org.vsu.rudakov.annotation.mapping.RequestBody;
import org.vsu.rudakov.annotation.mapping.UpdateMapping;
import org.vsu.rudakov.model.Educator;
import org.vsu.rudakov.repo.EducatorRepo;

import java.util.List;

@Controller
public class EducatorController {
    @Inject
    private EducatorRepo educatorRepo;

    @GetMapping(url = "educator/{id}")
    public Educator get(@PathVariable(name = "id") Long id) {
        return educatorRepo.getById(id);
    }

    @GetMapping(url = "educator")
    public List<Educator> getAll() {
        return educatorRepo.getAll();
    }

    @UpdateMapping(url = "educator")
    public Educator update(@RequestBody(name = "educator") Educator educator) {
        return educatorRepo.update(educator);
    }
}
