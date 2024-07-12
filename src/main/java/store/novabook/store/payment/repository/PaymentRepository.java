package store.novabook.store.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
