package com.example.eventbook_backend.specification;

import com.example.eventbook_backend.entity.Movie;
import com.example.eventbook_backend.enums.MovieGenre;
import com.example.eventbook_backend.enums.MovieStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class MovieSpecification {

    public static Specification<Movie> hasKeyword(String keyword){
        return (root, query, criteriaBuilder) -> {
            if(keyword == null || keyword.trim().isEmpty()){
                return null;
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + keyword.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Movie> hasGenre(MovieGenre genre){
        return (root, query, criteriaBuilder) -> {
            if(genre == null){
                return null;
            }

            Join<Movie, MovieGenre> genreJoin = root.join("genres", JoinType.INNER);

            return criteriaBuilder.equal(genreJoin,genre);
        };
    }

    public static Specification<Movie> hasLanguage(String language){
        return (root, query, criteriaBuilder) -> {

            if(language == null || language.trim().isEmpty()){
                return null;
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("language")),
                    language.toLowerCase()
            );
        };
    }

    public static Specification<Movie> hasCertification(String certification){
        return (root, query, criteriaBuilder) -> {

            if(certification == null || certification.trim().isBlank()){
                return null;
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("certification")),
                    certification.toLowerCase()
            );
        };
    }

    public static Specification<Movie> hasStatus(MovieStatus status){
         return (root, query, criteriaBuilder) -> {

             if(status == null){
                 return null;
             }

             return criteriaBuilder.equal(
                     root.get("status"), status
             );
         };
    }
}
