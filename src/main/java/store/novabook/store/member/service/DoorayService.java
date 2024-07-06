package store.novabook.store.member.service;

public interface DoorayService {
	void sendAuthCode(Long memberId, String authCode);
}
