FROM maven:3.9-eclipse-temurin-25
WORKDIR /app
RUN apt-get update && apt-get install -y \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgtk-3-0 \
    libglib2.0-0 \
    libgl1 \
    libglx0 \
    && rm -rf /var/lib/apt/lists/*
COPY pom.xml .
COPY . /app
RUN mvn package -DskipTests
CMD ["mvn", "javafx:run"]