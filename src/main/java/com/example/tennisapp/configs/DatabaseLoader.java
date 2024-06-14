package com.example.tennisapp.configs;

import com.example.tennisapp.daos.CourtDao;
import com.example.tennisapp.daos.UserDao;
import com.example.tennisapp.enums.Role;
import com.example.tennisapp.enums.Surface;
import com.example.tennisapp.models.Court;
import com.example.tennisapp.models.User;
import com.example.tennisapp.services.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseLoader {

    @Value("${app.insert-data}")
    private boolean insertData;

    @Value("${app.insert-more-data}")
    private boolean insertMoreData;

    private final CourtService courtService;
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner commandLineRunner(){
        return args -> {
            if (insertData) {
                Court court = new Court(Surface.GRASS);
                this.courtService.save(court);

                court = new Court(Surface.HARD);
                this.courtService.save(court);

                court = new Court(Surface.CLAY);
                this.courtService.save(court);

                court = new Court(Surface.ARTIFICIAL);
                court.setIsDeleted(true);
                this.courtService.save(court);

                if (insertMoreData) {
                    User user = new User(
                            "123456789",
                            "user",
                            passwordEncoder.encode("user"),
                            Role.USER,
                            false
                    );
                    userDao.save(user);

                    user = new User(
                            "987654321",
                            "admin",
                            passwordEncoder.encode("admin"),
                            Role.ADMIN,
                            false
                    );
                    userDao.save(user);
                }
            }
        };
    }
}
