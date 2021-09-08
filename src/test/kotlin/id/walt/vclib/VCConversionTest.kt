package id.walt.vclib

import com.nimbusds.jwt.SignedJWT
import id.walt.vclib.Helpers.toCredential
import id.walt.vclib.model.VerifiableCredential
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class VCConversionTest : StringSpec({

    "1. jwt parser test" {
        val jwt = "eyJraWQiOiJkaWQ6ZWJzaToyMndGN2tVREZUM3RrdTVxdzQ0TGRWdThvMmRKc1U0RkxOSjd6SzRLbmhlbUdLeDUiLCJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NksifQ.eyJpc3MiOiJkaWQ6ZWJzaToyMndGN2tVREZUM3RrdTVxdzQ0TGRWdThvMmRKc1U0RkxOSjd6SzRLbmhlbUdLeDUiLCJzdWIiOiJkaWQ6a2V5Ono2TWt2Z2hjaGtkdWZ3Y2RZZFBnc3h2YUhQZkU2WjZ3ZDQ0NndUaWp3algxaXFBciIsIm5iZiI6MTYzMTAwMzY2MiwiaWF0IjoxNjMxMDAzNjYyLCJ2YyI6eyJjcmVkZW50aWFsU2NoZW1hIjp7ImlkIjoiaHR0cHM6XC9cL2FwaS5wcmVwcm9kLmVic2kuZXVcL3RydXN0ZWQtc2NoZW1hcy1yZWdpc3RyeVwvdjFcL3NjaGVtYXNcLzB4YmY3OGZjMDhhN2E5ZjI4ZjU0NzlmNThkZWEyNjlkMzY1N2Y1NGYxM2NhMzdkMzgwY2Q0ZTkyMjM3ZmI2OTFkZCIsInR5cGUiOiJKc29uU2NoZW1hVmFsaWRhdG9yMjAxOCJ9LCJjcmVkZW50aWFsU3ViamVjdCI6eyJsZWFybmluZ1NwZWNpZmljYXRpb24iOnsiaXNjZWRmQ29kZSI6WyI3Il0sImVjdHNDcmVkaXRQb2ludHMiOjEyMCwiZXFmTGV2ZWwiOjcsImlkIjoiaHR0cHM6XC9cL2xlYXN0b24uYmNkaXBsb21hLmNvbVwvbGF3LWVjb25vbWljcy1tYW5hZ2VtZW50I0xlYXJuaW5nU3BlY2lmaWNhdGlvbiIsIm5xZkxldmVsIjpbIjciXX0sImlkZW50aWZpZXIiOiIwOTA0MDA4MDg0SCIsImF3YXJkaW5nT3Bwb3J0dW5pdHkiOnsiaWRlbnRpZmllciI6Imh0dHBzOlwvXC9jZXJ0aWZpY2F0ZS1kZW1vLmJjZGlwbG9tYS5jb21cL2NoZWNrXC84N0VEMkYyMjcwRTZDNDE0NTZFOTRCODZCOUQ5MTE1QjRFMzVCQ0NBRDIwMEE0OUI4NDY1OTJDMTRGNzlDODZCVjFGbmJsbHRhME5aVG5Ka1IzbERXbFJtVERsU1JVSkVWRlpJU21ObVl6SmhVVTVzWlVKNVoyRkpTSHBXYm1aWiIsImVuZGVkQXRUaW1lIjoiMjAyMC0wNi0yNlQwMDowMDowMFoiLCJzdGFydGVkQXRUaW1lIjoiMjAxOS0wOS0wMlQwMDowMDowMFoiLCJhd2FyZGluZ0JvZHkiOnsicmVnaXN0cmF0aW9uIjoiMDU5NzA2NUoiLCJpZCI6ImRpZDplYnNpOjIyd0Y3a1VERlQzdGt1NXF3NDRMZFZ1OG8yZEpzVTRGTE5KN3pLNEtuaGVtR0t4NSIsInByZWZlcnJlZE5hbWUiOiJMZWFzdG9uIFVuaXZlcnNpdHkiLCJlaWRhc0xlZ2FsSWRlbnRpZmllciI6IlVua25vd24iLCJob21lcGFnZSI6Imh0dHBzOlwvXC9sZWFzdG9uLmJjZGlwbG9tYS5jb21cLyJ9LCJsb2NhdGlvbiI6IkZSQU5DRSIsImlkIjoiaHR0cHM6XC9cL2xlYXN0b24uYmNkaXBsb21hLmNvbVwvbGF3LWVjb25vbWljcy1tYW5hZ2VtZW50I0F3YXJkaW5nT3Bwb3J0dW5pdHkifSwiZmFtaWx5TmFtZSI6IkRPRSIsImdpdmVuTmFtZXMiOiJKYW5lIiwiZGF0ZU9mQmlydGgiOiIxOTkzLTA0LTA4VDAwOjAwOjAwWiIsImxlYXJuaW5nQWNoaWV2ZW1lbnQiOnsiYWRkaXRpb25hbE5vdGUiOlsiRElTVFJJQlVUSU9OIE1BTkFHRU1FTlQiXSwiZGVzY3JpcHRpb24iOiJNQVJLRVRJTkcgQU5EIFNBTEVTIiwiaWQiOiJodHRwczpcL1wvbGVhc3Rvbi5iY2RpcGxvbWEuY29tXC9sYXctZWNvbm9taWNzLW1hbmFnZW1lbnQjTGVhcm5pbmdBY2hpZXZtZW50IiwidGl0bGUiOiJNQVNURVJTIExBVywgRUNPTk9NSUNTIEFORCBNQU5BR0VNRU5UIn0sImdyYWRpbmdTY2hlbWUiOnsiaWQiOiJodHRwczpcL1wvbGVhc3Rvbi5iY2RpcGxvbWEuY29tXC9sYXctZWNvbm9taWNzLW1hbmFnZW1lbnQjR3JhZGluZ1NjaGVtZSIsInRpdGxlIjoiTG93ZXIgU2Vjb25kLUNsYXNzIEhvbm91cnMifX0sInZhbGlkRnJvbSI6IjIwMjEtMDgtMzFUMDA6MDA6MDBaIiwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlZlcmlmaWFibGVBdHRlc3RhdGlvbiIsIlZlcmlmaWFibGVEaXBsb21hIl0sIkBjb250ZXh0IjpbImh0dHBzOlwvXC93d3cudzMub3JnXC8yMDE4XC9jcmVkZW50aWFsc1wvdjEiXX19.gtdEFs35FUsRz1PYElN99C4Y9QCYejqN3wNVkpYwKC4U8ACnnaDVP5q1fX007qAtA-f0Dm39iA9ShtQIjn_zHA"
        val cred = jwt.toCredential()
        VerifiableCredential::class.java.isAssignableFrom(cred::class.java) shouldBe true
        cred?.proof?.type shouldBe "JWT"
        cred?.proof?.jwt shouldBe jwt
        cred.json?.shouldEqualJson(SignedJWT.parse(jwt).jwtClaimsSet.claims["vc"].toString())
    }
}) {}