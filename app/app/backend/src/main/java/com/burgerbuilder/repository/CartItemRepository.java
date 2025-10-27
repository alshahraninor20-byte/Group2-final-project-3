package com.burgerbuilder.repository;

import com.burgerbuilder.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findBySessionIdOrderByCreatedAtDesc(String sessionId);
    
    Optional<CartItem> findBySessionIdAndIngredientId(String sessionId, Long ingredientId);
    
    @Query("SELECT ci FROM CartItem ci WHERE ci.sessionId = :sessionId AND ci.ingredient.id = :ingredientId")
    Optional<CartItem> findBySessionAndIngredient(@Param("sessionId") String sessionId, 
                                                  @Param("ingredientId") Long ingredientId);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") String sessionId);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.sessionId = :sessionId AND ci.id = :cartItemId")
    void deleteBySessionIdAndId(@Param("sessionId") String sessionId, @Param("cartItemId") Long cartItemId);
    
    @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.sessionId = :sessionId")
    Long countBySessionId(@Param("sessionId") String sessionId);
    
    @Query("SELECT SUM(ci.totalPrice) FROM CartItem ci WHERE ci.sessionId = :sessionId")
    java.math.BigDecimal getTotalPriceBySessionId(@Param("sessionId") String sessionId);
    
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.createdAt < :cutoffTime")
    void deleteExpiredCartItems(@Param("cutoffTime") LocalDateTime cutoffTime);
}
