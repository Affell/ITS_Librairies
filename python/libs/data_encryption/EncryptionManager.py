import base64

from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import padding


def encryptSecretKey(secretKey, publicKey) -> list:
    return [base64.b64encode(publicKey.encrypt(secretKey[i], padding=padding.OAEP(
        mgf=padding.MGF1(algorithm=hashes.SHA256()),
        algorithm=hashes.SHA256(),
        label=None
     ))).decode('utf-8') for i in range(2)]


def decryptSecretKey(secretKey: list, privateKey):
    return [privateKey.decrypt(base64.b64decode(byteElement.encode('utf-8')), padding=padding.OAEP(
        mgf=padding.MGF1(algorithm=hashes.SHA256()),
        algorithm=hashes.SHA256(),
        label=None
     )) for byteElement in secretKey]


def encrypt(data: str, secretKey):
    return base64.b64encode(secretKey.encrypt(data.encode('utf-8'))).decode('utf-8')


def decrypt(data: str, secretKey):
    try:
        return secretKey.decrypt(base64.b64decode(data.encode('utf-8'))).decode('utf-8')
    except (Exception,):
        return None
