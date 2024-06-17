package com.example.tennisapp.configs;

import com.example.tennisapp.daos.ReservationDao;
import com.example.tennisapp.daos.UserDao;
import com.example.tennisapp.enums.Role;
import com.example.tennisapp.enums.Surface;
import com.example.tennisapp.models.Court;
import com.example.tennisapp.models.Reservation;
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
    private final UserDao userDao; // allowing admin insertion2
    private final PasswordEncoder passwordEncoder;
    private final ReservationDao reservationDao;

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            if (insertData) {
                Court court = new Court(Surface.GRASS, 200.00);
                this.courtService.save(court);

                Court court2 = new Court(Surface.HARD, 250.00);
                this.courtService.save(court2);

                Court court3 = new Court(Surface.CLAY, 149.99);
                this.courtService.save(court3);

                Court court4 = new Court(Surface.ARTIFICIAL, 300.00);
                court4.setIsDeleted(true);
                this.courtService.save(court4);

                if (insertMoreData) {
                    User user = new User(
                            "123456789",
                            "user",
                            passwordEncoder.encode("user"),
                            Role.USER,
                            false
                    );
                    userDao.save(user);

                    User admin = new User(
                            "987654321",
                            "admin",
                            passwordEncoder.encode("admin"),
                            Role.ADMIN,
                            false
                    );
                    userDao.save(admin);

                    Reservation reservation = new Reservation(
                            user,
                            court,
                            java.sql.Date.valueOf("2025-8-8"),
                            java.sql.Time.valueOf("8:00:00"),
                            java.sql.Time.valueOf("11:00:00"),
                            false
                    );

                    reservationDao.save(reservation);

                    Reservation reservation2 = new Reservation(
                            user,
                            court2,
                            java.sql.Date.valueOf("2025-8-8"),
                            java.sql.Time.valueOf("8:00:00"),
                            java.sql.Time.valueOf("11:00:00"),
                            false
                    );

                    reservationDao.save(reservation2);

                    Reservation reservation3 = new Reservation(
                            user,
                            court2,
                            java.sql.Date.valueOf("2025-8-9"),
                            java.sql.Time.valueOf("14:00:00"),
                            java.sql.Time.valueOf("16:00:00"),
                            false
                    );

                    reservationDao.save(reservation3);

                    Reservation reservation4 = new Reservation(
                            user,
                            court3,
                            java.sql.Date.valueOf("2023-8-9"),
                            java.sql.Time.valueOf("15:00:00"),
                            java.sql.Time.valueOf("16:00:00"),
                            false
                    );

                    reservationDao.save(reservation4);

                    Reservation reservation5 = new Reservation(
                            user,
                            court3,
                            java.sql.Date.valueOf("2025-8-9"),
                            java.sql.Time.valueOf("15:00:00"),
                            java.sql.Time.valueOf("16:00:00"),
                            false
                    );
                    reservation5.setIsDeleted(true);

                    reservationDao.save(reservation5);
                }
            }
        };
    }
}
