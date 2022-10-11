package org.vsu.rudakov.controller;

import org.vsu.rudakov.annotation.Controller;
import org.vsu.rudakov.annotation.Inject;
import org.vsu.rudakov.annotation.mapping.*;
import org.vsu.rudakov.model.Child;
import org.vsu.rudakov.repo.ChildRepo;

import javax.swing.*;
import java.util.List;

@Controller
public class ChildController {
    @Inject
    private ChildRepo childRepo;

    @GetMapping(url = "child")
    public List<Child> getAll() {
        return childRepo.getAll();
    }

    @GetMapping(url = "child/{id}")
    public Child get(@PathVariable(name = "id") Long id) {
        return childRepo.getById(id);
    }

    @UpdateMapping(url = "child")
    public Child update(@RequestBody(name = "child") Child child) {
        return childRepo.update(child);
    }

    @DeleteMapping(url = "child/{id}")
    public boolean delete(@PathVariable(name = "id") Long id) {
        return childRepo.delete(id);
    }

    @PostMapping(url = "child")
    public boolean create(@RequestBody(name = "child") Child child) {
        return childRepo.create(child);
    }
}
