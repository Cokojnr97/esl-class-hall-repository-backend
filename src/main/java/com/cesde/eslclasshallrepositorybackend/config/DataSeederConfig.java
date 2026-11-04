package com.cesde.eslclasshallrepositorybackend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.cesde.eslclasshallrepositorybackend.model.entity.Administrator;
import com.cesde.eslclasshallrepositorybackend.model.entity.CourseClass;
import com.cesde.eslclasshallrepositorybackend.model.entity.Resource;
import com.cesde.eslclasshallrepositorybackend.model.entity.Teacher;
import com.cesde.eslclasshallrepositorybackend.model.entity.UserProfile;
import com.cesde.eslclasshallrepositorybackend.model.embeddable.Address;
import com.cesde.eslclasshallrepositorybackend.model.enums.CourseLevel;
import com.cesde.eslclasshallrepositorybackend.model.enums.ResourceStatus;
import com.cesde.eslclasshallrepositorybackend.model.enums.ResourceType;
import com.cesde.eslclasshallrepositorybackend.repository.CourseClassRepository;
import com.cesde.eslclasshallrepositorybackend.repository.ResourceRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UserProfileRepository;
import com.cesde.eslclasshallrepositorybackend.repository.UserRepository;

@Configuration
public class DataSeederConfig {

    private static final List<MockTeacherData> TEACHERS = List.of(
            new MockTeacherData("Laura Martínez", "laura.martinez@cesde.net", "+57 300 412 7831",
                    "English teacher focused on communicative learning and pronunciation.",
                    "Carrera 43A # 16 Sur - 48", "Medellín", "050022"),
            new MockTeacherData("Carlos Ramírez", "carlos.ramirez@cesde.net", "+57 301 528 1946",
                    "Teacher specializing in grammar, writing, and academic English.",
                    "Calle 10 # 36 - 16", "Medellín", "050021"),
            new MockTeacherData("Valentina Gómez", "valentina.gomez@cesde.net", "+57 302 637 4508",
                    "English teacher who designs interactive lessons for beginner learners.",
                    "Carrera 25 # 10 - 55", "Medellín", "050021"),
            new MockTeacherData("Santiago Rodríguez", "santiago.rodriguez@cesde.net", "+57 310 745 2169",
                    "Teacher focused on listening comprehension and real-world conversations.",
                    "Calle 44 # 70 - 18", "Medellín", "050031"),
            new MockTeacherData("Mariana López", "mariana.lopez@cesde.net", "+57 315 864 3027",
                    "Teacher and curriculum designer for reading and vocabulary development.",
                    "Carrera 32 # 8A - 42", "Medellín", "050021"),
            new MockTeacherData("Andrés Castillo", "andres.castillo@cesde.net", "+57 316 973 5814",
                    "Teacher experienced in project-based English and digital learning.",
                    "Calle 33 # 80 - 27", "Medellín", "050030"),
            new MockTeacherData("Natalia Herrera", "natalia.herrera@cesde.net", "+57 317 186 4925",
                    "Teacher specializing in English for professional and workplace contexts.",
                    "Carrera 48 # 52 - 20", "Medellín", "050012"),
            new MockTeacherData("Julián Torres", "julian.torres@cesde.net", "+57 318 294 6073",
                    "Teacher who creates accessible practice materials for all learning styles.",
                    "Calle 12 # 43D - 61", "Medellín", "050021"),
            new MockTeacherData("Daniela Vargas", "daniela.vargas@cesde.net", "+57 319 407 8152",
                    "Teacher focused on pronunciation, fluency, and confidence building.",
                    "Carrera 70 # 45E - 12", "Medellín", "050034"),
            new MockTeacherData("Felipe Moreno", "felipe.moreno@cesde.net", "+57 320 518 9364",
                    "Teacher and assessment designer for CEFR-aligned English courses.",
                    "Calle 9 # 30 - 17", "Medellín", "050021"));

    private static final List<ResourceData> RESOURCES = List.of(
            new ResourceData("Everyday English: Meeting New People",
                    "Practice greetings, introductions, and polite questions for everyday conversations.",
                    "https://learnenglish.britishcouncil.org/skills/speaking/a1-speaking/meeting-new-people",
                    ResourceType.ENLACE),
            new ResourceData("Present Simple Grammar Guide",
                    "A concise guide with examples and exercises for routines, facts, and habits.",
                    "https://learnenglish.britishcouncil.org/grammar/a1-a2-grammar/present-simple",
                    ResourceType.DOCUMENTO),
            new ResourceData("Listening Practice: A Day at Work",
                    "An audio lesson for identifying times, activities, and workplace expressions.",
                    "https://learnenglish.britishcouncil.org/skills/listening/a2-listening/day-work",
                    ResourceType.AUDIO),
            new ResourceData("Food and Cooking Vocabulary",
                    "Build practical vocabulary for ingredients, recipes, and describing meals.",
                    "https://learnenglish.britishcouncil.org/vocabulary/a1-a2/food-and-cooking",
                    ResourceType.VIDEO),
            new ResourceData("Past Experiences Reading Pack",
                    "Reading activities that develop comprehension of personal stories and past events.",
                    "https://learnenglish.britishcouncil.org/skills/reading/b1-reading/past-experiences",
                    ResourceType.DOCUMENTO),
            new ResourceData("Pronunciation: The Sounds of English",
                    "Video lessons for practicing vowel and consonant sounds with example words.",
                    "https://www.youtube.com/watch?v=qqLi1f3r7cU",
                    ResourceType.VIDEO),
            new ResourceData("Travel English Phrasebook",
                    "Useful expressions for airports, hotels, restaurants, and asking for directions.",
                    "https://learnenglish.britishcouncil.org/vocabulary/a1-a2/travel",
                    ResourceType.DOCUMENTO),
            new ResourceData("Conversation Cards: Weekend Plans",
                    "Speaking prompts that help learners ask follow-up questions and keep conversations going.",
                    "https://learnenglish.britishcouncil.org/skills/speaking/b1-speaking/weekend-plans",
                    ResourceType.IMAGEN),
            new ResourceData("Modal Verbs for Advice and Obligation",
                    "Grammar exercises covering should, must, have to, and useful everyday advice.",
                    "https://learnenglish.britishcouncil.org/grammar/b1-b2-grammar/modals",
                    ResourceType.ENLACE),
            new ResourceData("B1 Vocabulary Review Quiz",
                    "A self-check quiz covering common vocabulary for work, study, and daily life.",
                    "https://learnenglish.britishcouncil.org/vocabulary/b1-b2-vocabulary/review-quiz",
                    ResourceType.OTRO));

    @Bean
    public CommandLineRunner seedDatabase(
            UserRepository userRepository,
            UserProfileRepository profileRepository,
            CourseClassRepository courseClassRepository,
            ResourceRepository resourceRepository) {
        return args -> {
            if (userRepository.findByEmailIgnoreCase("jvelasquezan1@cesde.net").isEmpty()) {
                Administrator admin = userRepository.save(new Administrator(
                        "Andrés Velásquez",
                        "jvelasquezan1@cesde.net",
                        "admin123"));
                profileRepository.save(new UserProfile(
                        "Administrator of the ESL Class Hall repository.",
                        "+57 300 000 0000",
                        admin));
            }

            var existingLevels = courseClassRepository.findAll().stream()
                    .map(CourseClass::getLevel)
                    .collect(java.util.stream.Collectors.toSet());
            Arrays.stream(CourseLevel.values())
                    .filter(level -> !existingLevels.contains(level))
                    .forEach(level -> {
                    courseClassRepository.save(new CourseClass(
                            level,
                            1,
                            level + " foundation unit",
                            "Academy-managed resources for " + level + " learners."));
                    });

            List<Teacher> mockTeachers = IntStream.range(0, TEACHERS.size())
                    .mapToObj(index -> seedTeacher(index, userRepository, profileRepository))
                    .toList();
            List<CourseClass> courseClasses = courseClassRepository.findAll();
            IntStream.range(0, 40).forEach(index -> seedResource(
                    index,
                    mockTeachers,
                    courseClasses,
                    resourceRepository));
        };
    }

    private Teacher seedTeacher(
            int index,
            UserRepository userRepository,
            UserProfileRepository profileRepository) {
        MockTeacherData data = TEACHERS.get(index);
        String legacyEmail = "mock.teacher" + (index + 1) + "@mock.cesde.net";
        Teacher teacher = (Teacher) userRepository.findByEmailIgnoreCase(data.email())
                .orElseGet(() -> userRepository.findByEmailIgnoreCase(legacyEmail)
                        .map(existing -> {
                            existing.setName(data.name());
                            existing.setEmail(data.email());
                            return userRepository.save(existing);
                        })
                        .orElseGet(() -> userRepository.save(new Teacher(
                                data.name(),
                                data.email(),
                                "mock-password-" + (index + 1)))));

        if (teacher.getProfile() == null) {
            profileRepository.save(new UserProfile(data.biography(), data.phone(), teacher));
        } else {
            teacher.getProfile().setBiography(data.biography());
            teacher.getProfile().setPhone(data.phone());
        }
        teacher.setAddress(new Address(data.street(), data.city(), data.postalCode(), "Colombia"));
        userRepository.save(teacher);
        return teacher;
    }

    private void seedResource(
            int index,
            List<Teacher> mockTeachers,
            List<CourseClass> courseClasses,
            ResourceRepository resourceRepository) {
        ResourceData data = RESOURCES.get(index % RESOURCES.size());
        int edition = index / RESOURCES.size() + 1;
        String title = edition == 1 ? data.title() : data.title() + " - Practice " + edition;
        Resource resource = resourceRepository.findByTitle(title)
                .orElseGet(() -> resourceRepository.findByTitle(
                        "[MOCK] English practice resource " + String.format("%02d", index + 1))
                        .orElseGet(() -> new Resource(
                                title,
                                data.description(),
                                data.url(),
                                data.type(),
                                mockTeachers.get(index % mockTeachers.size()))));
        resource.setTitle(title);
        resource.setDescription(data.description());
        resource.setUrl(data.url());
        resource.setType(data.type());
        resource.setCreatedBy(mockTeachers.get(index % mockTeachers.size()));
        resource.setStatus(ResourceStatus.PUBLISHED);
        resource.setCourseUnits(Set.of(courseClasses.get(index % courseClasses.size())));
        resourceRepository.save(resource);
    }

    private record MockTeacherData(
            String name,
            String email,
            String phone,
            String biography,
            String street,
            String city,
            String postalCode) {
    }

    private record ResourceData(String title, String description, String url, ResourceType type) {
    }
}
