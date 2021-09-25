from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.serialization import PublicFormat, Encoding
import base64


class KeyGenerator:
    def __init__(self, keySize: int = 2048, algorithm: str = "RSA"):
        self.secretKeyBytes = None
        self.secretKeyNonce = None
        self.publicKey = None
        self.privateKey = None
        self.keySize = keySize
        self.algorithm = algorithm

    def generateKeys(self, secretKey: bool, asymetricalKeys: bool):
        if secretKey:
            self.secretKeyBytes = get_random_bytes(16)
            self.secretKeyNonce = AES.new(self.secretKeyBytes, AES.MODE_EAX).nonce
        if asymetricalKeys:
            self.privateKey = rsa.generate_private_key(public_exponent=65537, key_size=2048, backend=default_backend())
            self.publicKey = self.privateKey.public_key()

    def getSecretKeyBytes(self):
        return self.secretKeyBytes, self.secretKeyNonce

    def getSecretKey(self):
        return AES.new(self.secretKeyBytes, AES.MODE_EAX, self.secretKeyNonce)

    def getStringPublicKey(self):
        return base64.b64encode(self.publicKey.public_bytes(encoding=Encoding.DER, format=PublicFormat.SubjectPublicKeyInfo)).decode(
            'utf-8')
