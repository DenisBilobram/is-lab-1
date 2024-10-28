package is.lab1.is_lab1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import is.lab1.is_lab1.model.Car;
import is.lab1.is_lab1.model.Coordinates;
import is.lab1.is_lab1.model.HumanBeing;
import is.lab1.is_lab1.model.Mood;
import is.lab1.is_lab1.model.WeaponType;
import jakarta.transaction.Transactional;

public interface HumanBeingRepository extends JpaRepository<HumanBeing, Long> {
    
    public List<HumanBeing> findByCoordinates(Coordinates coordinates);

    public List<HumanBeing> findByCar(Car car);

    public List<HumanBeing> findByRealHeroAndHasToothpick(Boolean realHero, Boolean hasToothpick);

    @Query("SELECT COUNT(h) FROM HumanBeing h WHERE h.impactSpeed = :impactSpeed")
    public long countByImpactSpeed(@Param("impactSpeed") int impactSpeed);

    @Query("SELECT h FROM HumanBeing h WHERE h.name LIKE %:substring%")
    public List<HumanBeing> findByNameContaining(@Param("substring") String substring);

    @Query("SELECT h FROM HumanBeing h WHERE h.weaponType > :weaponType")
    public List<HumanBeing> findByWeaponTypeGreaterThan(@Param("weaponType") WeaponType weaponType);

    @Transactional
    @Modifying
    @Query("DELETE FROM HumanBeing h WHERE h.realHero = true AND h.hasToothpick = false")
    public void deleteHerosByHasToothpickFalse();

    @Transactional
    @Modifying
    @Query("UPDATE HumanBeing h SET h.mood = :newMood")
    public int updateMoodForAll(@Param("newMood") Mood newMood);

}
