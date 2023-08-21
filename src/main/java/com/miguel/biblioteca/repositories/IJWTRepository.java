package com.miguel.biblioteca.repositories;

import com.miguel.biblioteca.model.JWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IJWTRepository extends JpaRepository<JWT, Integer> {

    @Query(value = """
        select j from JWT j 
        inner join ULibrarian u
        on j.uLibrarian.idUser = u.idUser        
        where u.idUser = :idLibrarian and (j.expired = false or j.revoked = false)
    """)
    List<JWT> findAllValidTokenByUser(@Param("idLibrarian") Integer id);

    Optional<JWT> findByToken(String token);
}
