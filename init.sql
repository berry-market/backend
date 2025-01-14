-- 사용자 berry 생성 및 비밀번호 설정
CREATE USER IF NOT EXISTS 'berry'@'%' IDENTIFIED BY 'berry';

-- 각 서비스용 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS `user-service`;
CREATE DATABASE IF NOT EXISTS `post-service`;
CREATE DATABASE IF NOT EXISTS `payment-service`;
CREATE DATABASE IF NOT EXISTS `delivery-service`;
CREATE DATABASE IF NOT EXISTS `bid-service`;

-- 데이터베이스 권한 부여
GRANT ALL PRIVILEGES ON `user-service`.* TO 'berry'@'%';
GRANT ALL PRIVILEGES ON `post-service`.* TO 'berry'@'%';
GRANT ALL PRIVILEGES ON `payment-service`.* TO 'berry'@'%';
GRANT ALL PRIVILEGES ON `delivery-service`.* TO 'berry'@'%';
GRANT ALL PRIVILEGES ON `bid-service`.* TO 'berry'@'%';

-- 변경 사항 적용
FLUSH PRIVILEGES;
