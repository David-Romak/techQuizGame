package com.romac.techquizgame.service;

import com.romac.techquizgame.model.Tech;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Single source of truth for all technologies in the quiz.
 * Hey, what we're doing here is: using @Component so Spring registers this
 * as a bean — meaning Spring creates ONE instance and injects it wherever needed.
 * This is Dependency Injection (DI) in action.
 *
 * All icons use the devicons CDN — 200+ tech logos, no assets to bundle.
 */
@Component
public class TechLibrary {

    private static final String CDN = "https://cdn.jsdelivr.net/gh/devicons/devicon/icons";

    public List<Tech> getAllTechs() {
        return List.of(
            // ── Languages ──────────────────────────────────────────────────────
            tech("JavaScript",   "javascript/javascript-original.svg",                   List.of("js"),              "Language"),
            tech("TypeScript",   "typescript/typescript-original.svg",                   List.of("ts"),              "Language"),
            tech("Python",       "python/python-original.svg",                           List.of(),                  "Language"),
            tech("Rust",         "rust/rust-original.svg",                               List.of(),                  "Language"),
            tech("Go",           "go/go-original-wordmark.svg",                          List.of("golang"),          "Language"),
            tech("Java",         "java/java-original.svg",                               List.of(),                  "Language"),
            tech("C",            "c/c-original.svg",                                     List.of(),                  "Language"),
            tech("C++",          "cplusplus/cplusplus-original.svg",                     List.of("cpp"),             "Language"),
            tech("C#",           "csharp/csharp-original.svg",                           List.of("csharp"),          "Language"),
            tech("PHP",          "php/php-original.svg",                                 List.of(),                  "Language"),
            tech("Ruby",         "ruby/ruby-original.svg",                               List.of(),                  "Language"),
            tech("Swift",        "swift/swift-original.svg",                             List.of(),                  "Language"),
            tech("Kotlin",       "kotlin/kotlin-original.svg",                           List.of(),                  "Language"),
            tech("Dart",         "dart/dart-original.svg",                               List.of(),                  "Language"),
            tech("Scala",        "scala/scala-original.svg",                             List.of(),                  "Language"),
            tech("Elixir",       "elixir/elixir-original.svg",                           List.of(),                  "Language"),
            tech("Haskell",      "haskell/haskell-original.svg",                         List.of(),                  "Language"),
            tech("R",            "r/r-original.svg",                                     List.of(),                  "Language"),
            tech("Lua",          "lua/lua-original.svg",                                 List.of(),                  "Language"),

            // ── Frontend Frameworks ────────────────────────────────────────────
            tech("React",        "react/react-original.svg",                             List.of("reactjs"),         "Frontend"),
            tech("Vue",          "vuejs/vuejs-original.svg",                             List.of("vuejs", "vue.js"), "Frontend"),
            tech("Angular",      "angularjs/angularjs-original.svg",                     List.of("angularjs"),       "Frontend"),
            tech("Svelte",       "svelte/svelte-original.svg",                           List.of(),                  "Frontend"),
            tech("Next.js",      "nextjs/nextjs-original.svg",                           List.of("nextjs", "next"),  "Frontend"),
            tech("Nuxt.js",      "nuxtjs/nuxtjs-original.svg",                           List.of("nuxtjs", "nuxt"), "Frontend"),
            tech("Bootstrap",    "bootstrap/bootstrap-original.svg",                     List.of(),                  "Frontend"),
            tech("Tailwind CSS", "tailwindcss/tailwindcss-original.svg",                 List.of("tailwind"),        "Frontend"),
            tech("Sass",         "sass/sass-original.svg",                               List.of("scss"),            "Frontend"),

            // ── Backend Frameworks ─────────────────────────────────────────────
            tech("Node.js",      "nodejs/nodejs-original.svg",                           List.of("node", "nodejs"),  "Backend"),
            tech("Express",      "express/express-original.svg",                         List.of("expressjs"),       "Backend"),
            tech("Django",       "django/django-plain.svg",                              List.of(),                  "Backend"),
            tech("Flask",        "flask/flask-original.svg",                             List.of(),                  "Backend"),
            tech("FastAPI",      "fastapi/fastapi-original.svg",                         List.of(),                  "Backend"),
            tech("Laravel",      "laravel/laravel-original.svg",                         List.of(),                  "Backend"),
            tech("Spring",       "spring/spring-original.svg",                           List.of("spring boot"),     "Backend"),
            tech("Rails",        "rails/rails-original-wordmark.svg",                    List.of("ruby on rails"),   "Backend"),
            tech("NestJS",       "nestjs/nestjs-original.svg",                           List.of("nest"),            "Backend"),

            // ── Databases ──────────────────────────────────────────────────────
            tech("PostgreSQL",   "postgresql/postgresql-original.svg",                   List.of("postgres"),        "Database"),
            tech("MySQL",        "mysql/mysql-original.svg",                             List.of(),                  "Database"),
            tech("MongoDB",      "mongodb/mongodb-original.svg",                         List.of("mongo"),           "Database"),
            tech("Redis",        "redis/redis-original.svg",                             List.of(),                  "Database"),
            tech("SQLite",       "sqlite/sqlite-original.svg",                           List.of(),                  "Database"),
            tech("Cassandra",    "cassandra/cassandra-original.svg",                     List.of(),                  "Database"),

            // ── DevOps / Cloud ─────────────────────────────────────────────────
            tech("Docker",       "docker/docker-original.svg",                           List.of(),                  "DevOps"),
            tech("Kubernetes",   "kubernetes/kubernetes-original.svg",                   List.of("k8s"),             "DevOps"),
            tech("Git",          "git/git-original.svg",                                 List.of(),                  "DevOps"),
            tech("GitHub",       "github/github-original.svg",                           List.of(),                  "DevOps"),
            tech("GitLab",       "gitlab/gitlab-original.svg",                           List.of(),                  "DevOps"),
            tech("Jenkins",      "jenkins/jenkins-original.svg",                         List.of(),                  "DevOps"),
            tech("Nginx",        "nginx/nginx-original.svg",                             List.of(),                  "DevOps"),
            tech("Ansible",      "ansible/ansible-original.svg",                         List.of(),                  "DevOps"),
            tech("Terraform",    "terraform/terraform-original.svg",                     List.of(),                  "DevOps"),
            tech("Linux",        "linux/linux-original.svg",                             List.of(),                  "DevOps"),
            tech("Ubuntu",       "ubuntu/ubuntu-original.svg",                           List.of(),                  "DevOps"),
            tech("AWS",          "amazonwebservices/amazonwebservices-original-wordmark.svg", List.of("amazon web services"), "DevOps"),
            tech("Azure",        "azure/azure-original.svg",                             List.of("microsoft azure"),  "DevOps"),
            tech("Google Cloud", "googlecloud/googlecloud-original.svg",                 List.of("gcp"),             "DevOps"),
            tech("Firebase",     "firebase/firebase-original.svg",                       List.of(),                  "DevOps"),

            // ── Tools / IDEs ───────────────────────────────────────────────────
            tech("VS Code",      "vscode/vscode-original.svg",                           List.of("vscode", "visual studio code"), "Tool"),
            tech("Vim",          "vim/vim-original.svg",                                 List.of(),                  "Tool"),
            tech("Bash",         "bash/bash-original.svg",                               List.of("shell"),           "Tool"),
            tech("Webpack",      "webpack/webpack-original.svg",                         List.of(),                  "Tool"),
            tech("Vite",         "vite/vite-original.svg",                               List.of(),                  "Tool"),
            tech("Babel",        "babel/babel-original.svg",                             List.of(),                  "Tool"),
            tech("GraphQL",      "graphql/graphql-plain.svg",                            List.of(),                  "Tool"),
            tech("Jest",         "jest/jest-plain.svg",                                  List.of(),                  "Tool"),
            tech("Figma",        "figma/figma-original.svg",                             List.of(),                  "Tool"),
            tech("Flutter",      "flutter/flutter-original.svg",                         List.of(),                  "Tool"),

            // ── Data / ML ──────────────────────────────────────────────────────
            tech("TensorFlow",   "tensorflow/tensorflow-original.svg",                   List.of(),                  "ML"),
            tech("PyTorch",      "pytorch/pytorch-original.svg",                         List.of(),                  "ML"),
            tech("NumPy",        "numpy/numpy-original.svg",                             List.of(),                  "ML"),
            tech("Pandas",       "pandas/pandas-original.svg",                           List.of(),                  "ML"),
            tech("Jupyter",      "jupyter/jupyter-original.svg",                         List.of(),                  "ML"),
            tech("OpenCV",       "opencv/opencv-original.svg",                           List.of(),                  "ML")
        );
    }

    /** Helper builder to keep the list readable */
    private Tech tech(String name, String iconPath, List<String> aliases, String category) {
        return new Tech(name, CDN + "/" + iconPath, aliases, category);
    }
}
